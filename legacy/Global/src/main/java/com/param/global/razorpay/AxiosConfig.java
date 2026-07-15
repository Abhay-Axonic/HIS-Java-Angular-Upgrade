package com.param.global.razorpay;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AxiosConfig {

    @Value("${razorpay.key.id}")
    private String razorpayKeyId;

    @Value("${razorpay.secret.id}")
    private String razorpaySecretId;

    @Value("${axios.senderId}")
    private String axiosSenderId;

    @Value("${axios.mailId}")
    private String axiosMailId;

    @Value("${axios.mailPassword}")
    private String axiosMailPassword;

    @Value("${axios.smtp.host}")
    private String axiosSmtpHost;

    @Value("${axios.smtp.port}")
    private String axiosSmtpPort;

    public String getRazorpayKeyId() {
        return razorpayKeyId;
    }

    public void setRazorpayKeyId(String razorpayKeyId) {
        this.razorpayKeyId = razorpayKeyId;
    }

    public String getRazorpaySecretId() {
        return razorpaySecretId;
    }

    public void setRazorpaySecretId(String razorpaySecretId) {
        this.razorpaySecretId = razorpaySecretId;
    }

    public String getAxiosSenderId() {
        return axiosSenderId;
    }

    public void setAxiosSenderId(String axiosSenderId) {
        this.axiosSenderId = axiosSenderId;
    }

    public String getAxiosMailId() {
        return axiosMailId;
    }

    public void setAxiosMailId(String axiosMailId) {
        this.axiosMailId = axiosMailId;
    }

    public String getAxiosMailPassword() {
        return axiosMailPassword;
    }

    public void setAxiosMailPassword(String axiosMailPassword) {
        this.axiosMailPassword = axiosMailPassword;
    }

    public String getAxiosSmtpHost() {
        return axiosSmtpHost;
    }

    public void setAxiosSmtpHost(String axiosSmtpHost) {
        this.axiosSmtpHost = axiosSmtpHost;
    }

    public String getAxiosSmtpPort() {
        return axiosSmtpPort;
    }

    public void setAxiosSmtpPort(String axiosSmtpPort) {
        this.axiosSmtpPort = axiosSmtpPort;
    }
}
