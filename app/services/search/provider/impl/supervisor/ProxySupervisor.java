package services.search.provider.impl.supervisor;

import play.Logger;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * ProxySupervisor ...
 *
 * @author vadim
 * @date 1/9/13
 */
public class ProxySupervisor {

    private final MailService mailService;
    private final Callable<Boolean> pingTask;
    private final int checkDelay;
    private final int failsLimit;
    private final AtomicInteger fails = new AtomicInteger(0);
    private volatile boolean available = true;
    private volatile boolean checking = false;
    private final ScheduledExecutorService pinger = Executors.newScheduledThreadPool(1);
    private final ExecutorService mailer = Executors.newFixedThreadPool(1);
    private ScheduledFuture supervisorFuture;
    private final String email;
    private final String name;

    public ProxySupervisor(MailService mailService, PingTask pingTask, int checkDelay, int failsLimit, String email) {
        this.mailService = mailService;
        this.pingTask = pingTask;
        this.checkDelay = checkDelay;
        this.failsLimit = failsLimit;
        this.email = email;
        this.name = pingTask.ofEngine();
    }

    /**
     * Check if proxy is potentially available. Proxy is marked as unavailable
     * when limit of access fails achieved or parse error was detected. When
     * proxy is marked as unavailable because of access error, a new thread will
     * start. It's job is to ping proxy time after time and mark it as available
     * when it'll be available again
     *
     * @return
     */
    public boolean isProxyAvailable() {
        return available;
    }

    public int getCheckDelay() {
        return checkDelay;
    }

    public int getFailsLimit() {
        return failsLimit;
    }

    /**
     * Method to indicate that request to proxy was failed.
     *
     * @param details of fail.
     */
    public void fail(String details) {
        Logger.debug(String.format("Number of current fails for %s proxy: %s", name, fails.get()));
        if (fails.incrementAndGet() >= failsLimit) {
            available = false;
            if (!checking) {
                checking = true;
                Runnable supervisor = new Runnable() {

                    @Override
                    public void run() {
                        try {
                            if (pingTask.call()) {
                                supervisorFuture.cancel(false);
                                checking = false;
                                available = true;
                                fails.set(0);
                                Logger.info("Proxy is available again");
                            } else {
                                Logger.warn("Proxy is unavailable");
                            }
                        } catch (Exception e) {
                            Logger.warn("Can't ping proxy because of: " + e.getLocalizedMessage());
                        }
                    }
                };
                supervisorFuture = pinger.scheduleWithFixedDelay(supervisor, 0L, checkDelay, TimeUnit.SECONDS);
                asyncMail(email, "proxy supervisor warning", String.format("Proxy %s is not available, limit %s exceeded", name, failsLimit));
            }
        }
    }

    private void asyncMail(final String email, final String subject, final String message) {
        Runnable mail = new Runnable() {
            @Override
            public void run() {
                try {
                    mailService.mailTo(email, subject, message);
                } catch (Throwable e) {
                    Logger.warn(e.getLocalizedMessage());
                }
            }
        };
        mailer.execute(mail);
    }
}
