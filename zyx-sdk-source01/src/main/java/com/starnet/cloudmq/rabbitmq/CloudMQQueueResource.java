package com.starnet.cloudmq.rabbitmq;

public class CloudMQQueueResource {
    Integer max_length_left;
    Integer max_length;
    Integer max_length_bytes_left;
    Integer max_length_bytes;
    Double alarm_threshold;
    Integer overflow_handle;
    Boolean msg_durable;
    
    public void setMax_length(Integer max_length) {
        this.max_length = max_length;
    }

    public void setMax_length_left(Integer max_length_left) {
        this.max_length_left = max_length_left;
    }

    public void setMax_length_bytes(Integer max_length_bytes) {
        this.max_length_bytes = max_length_bytes;
    }

    public void setMax_length_bytes_left(Integer max_length_bytes_left) {
        this.max_length_bytes_left = max_length_bytes_left;
    }

    public void setAlarm_threshold(double alarm_threshold) {
        this.alarm_threshold = alarm_threshold;
    }

	public Boolean getMsg_durable() {
		return msg_durable;
	}

	public void setMsg_durable(Boolean msg_durable) {
		this.msg_durable = msg_durable;
	}

	public void setOverflow_handle(Integer overflow_handle) {
        this.overflow_handle = overflow_handle;
    }

    public Integer getMax_length() {
        return max_length;
    }

    public Integer getMax_length_left() {
        return max_length_left;
    }

    public Integer getMax_length_bytes() {
        return max_length_bytes;
    }

    public Integer getMax_length_bytes_left() {
        return max_length_bytes_left;
    }

    public Double getAlarm_threshold() {
        return alarm_threshold;
    }

    public Integer getOverflow_handle() {
        return overflow_handle;
    }
}
