package com.starnet.cloudmq.rabbitmq;

public class CloudMQClientConfiguration {
    private Long recoveryInterval;
    private Integer concurrentConsumers;
    private Integer maxConcurrentConsumers;
    private Integer startConsumerMinInterval;
    private Integer stopConsumerMinInterval;
    private Integer consecutiveActiveTrigger;
    private Integer consecutiveIdleTrigger;
    private Long receiveTimeout;
    private Long shutdownTimeout;
    private Integer prefetchCount;
    private Integer txSize;
    private Integer declarationRetries;
    private Long failedDeclarationRetryInterval;
    private Long retryDeclarationInterval;

    public void setRecoveryInterval(long recoveryInterval) {
        this.recoveryInterval = recoveryInterval;
    }

    public void setConcurrentConsumers(int concurrentConsumers) {
        this.concurrentConsumers = concurrentConsumers;
    }

    public void setMaxConcurrentConsumers(int maxConcurrentConsumers) {
        this.maxConcurrentConsumers = maxConcurrentConsumers;
    }

    public void setStartConsumerMinInterval(int startConsumerMinInterval) {
        this.startConsumerMinInterval = startConsumerMinInterval;
    }

    public void setStopConsumerMinInterval(int stopConsumerMinInterval) {
        this.stopConsumerMinInterval = stopConsumerMinInterval;
    }

    public void setConsecutiveActiveTrigger(int consecutiveActiveTrigger) {
        this.consecutiveActiveTrigger = consecutiveActiveTrigger;
    }

    public void setConsecutiveIdleTrigger(int consecutiveIdleTrigger) {
        this.consecutiveIdleTrigger = consecutiveIdleTrigger;
    }

    public void setReceiveTimeout(long receiveTimeout) {
        this.receiveTimeout = receiveTimeout;
    }

    public void setShutdownTimeout(long shutdownTimeout) {
        this.shutdownTimeout = shutdownTimeout;
    }

    public void setPrefetchCount(int prefetchCount) {
        this.prefetchCount = prefetchCount;
    }

    public void setTxSize(int txSize) {
        this.txSize = txSize;
    }

    public void setDeclarationRetries(int declarationRetries) {
        this.declarationRetries = declarationRetries;
    }

    public void setFailedDeclarationRetryInterval(long failedDeclarationRetryInterval) {
        this.failedDeclarationRetryInterval = failedDeclarationRetryInterval;
    }

    public void setRetryDeclarationInterval(long retryDeclarationInterval) {
        this.retryDeclarationInterval = retryDeclarationInterval;
    }

    public Long getRecoveryInterval() {
        return recoveryInterval;
    }

    public Long getRetryDeclarationInterval() {
        return retryDeclarationInterval;
    }

    public Long getFailedDeclarationRetryInterval() {
        return failedDeclarationRetryInterval;
    }

    public Integer getStartConsumerMinInterval() {
        return startConsumerMinInterval;
    }

    public Integer getStopConsumerMinInterval() {
        return stopConsumerMinInterval;
    }

    public Integer getConcurrentConsumers() {
        return concurrentConsumers;
    }

    public Integer getMaxConcurrentConsumers() {
        return maxConcurrentConsumers;
    }

    public Integer getConsecutiveActiveTrigger() {
        return consecutiveActiveTrigger;
    }

    public Integer getConsecutiveIdleTrigger() {
        return consecutiveIdleTrigger;
    }

    public Integer getDeclarationRetries() {
        return declarationRetries;
    }

    public Long getReceiveTimeout() {
        return receiveTimeout;
    }

    public Long getShutdownTimeout() {
        return shutdownTimeout;
    }

    public Integer getTxSize() {
        return txSize;
    }

    public Integer getPrefetchCount() {
        return prefetchCount;
    }
}
