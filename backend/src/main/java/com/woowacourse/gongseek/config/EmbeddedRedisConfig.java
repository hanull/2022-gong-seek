package com.woowacourse.gongseek.config;

import com.woowacourse.gongseek.config.exception.NotFoundProcessException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Locale;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.util.StringUtils;
import redis.embedded.RedisServer;

@Slf4j
@Profile({"local", "test"})
@Configuration
public class EmbeddedRedisConfig {

    private static final String WINDOW_FIND_PORT_COMMAND = "netstat -nao | find \"LISTEN\" | find \"%d\"";
    private static final String MAC_FIND_PORT_COMMAND = "netstat -nat | grep LISTEN | grep %d";
    private static final int MAX_PORT_NUMBER = 65535;
    private static final int MIN_PORT_NUMBER = 10000;

    @Value("${spring.redis.port}")
    private int redisPort;

    private RedisServer redisServer;

    @PostConstruct
    public void redisServer() {
        int port = redisPort;
        if (isRedisRunning()) {
            port = findAvailablePort();
        }
        redisServer = new RedisServer(port);
        redisServer.start();
    }

    @PreDestroy
    public void stopRedis() {
        if (redisServer != null) {
            redisServer.stop();
        }
    }

    private boolean isRedisRunning() {
        return isRunning(executeGrepProcess(redisPort));
    }

    public int findAvailablePort() {
        for (int port = MIN_PORT_NUMBER; port <= MAX_PORT_NUMBER; port++) {
            Process process = executeGrepProcess(port);
            if (!isRunning(process)) {
                return port;
            }
        }
        log.info("이용 가능한 port를 찾지 못했습니다. port: 10000 ~ 65535");
        throw new NotFoundProcessException();
    }

    private Process executeGrepProcess(int port) {
        String osName = System.getProperty("os.name").toLowerCase(Locale.ROOT);
        String command = String.format(MAC_FIND_PORT_COMMAND, port);
        String[] shell = {"/bin/sh", "-c", command};

        if (osName.startsWith("win")) {
            command = String.format(WINDOW_FIND_PORT_COMMAND, port);
            shell = new String[]{"cmd.exe", "/y", "/c", command};
        }

        try {
            return Runtime.getRuntime().exec(shell);
        } catch (IOException e) {
            log.info("process 찾기에 실패했습니다. port:{} error: {}, {}", port, e, e.getMessage());
            throw new NotFoundProcessException();
        }
    }

    private boolean isRunning(Process process) {
        String line;
        StringBuilder pidInfo = new StringBuilder();

        try (BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            while ((line = input.readLine()) != null) {
                pidInfo.append(line);
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        return StringUtils.hasText(pidInfo.toString());
    }
}
