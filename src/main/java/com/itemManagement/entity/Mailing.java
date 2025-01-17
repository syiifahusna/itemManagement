package com.itemManagement.entity;

import com.itemManagement.audit.Auditable;
import jakarta.persistence.*;

@Entity
@Table(name = "mailings")
public class Mailing extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String host;
    @Column(nullable = false)
    private String port;
    @Column(nullable = false)
    private String username;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private String protocol;
    @Column(nullable = false)
    private boolean auth;
    @Column(nullable = false)
    private boolean isTlsEnabled;
    @Column(nullable = false)
    private boolean isTlsRequired;
    @Column(nullable = false)
    private boolean isDebugEnabled;

    public Mailing(){};

    public Mailing(String host, String port, String username, String password, String protocol, boolean auth, boolean isTlsEnabled, boolean isTlsRequired, boolean isDebugEnabled) {
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;
        this.protocol = protocol;
        this.auth = auth;
        this.isTlsEnabled = isTlsEnabled;
        this.isTlsRequired = isTlsRequired;
        this.isDebugEnabled = isDebugEnabled;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public boolean isAuth() {
        return auth;
    }

    public void setAuth(boolean auth) {
        this.auth = auth;
    }

    public boolean isTlsEnabled() {
        return isTlsEnabled;
    }

    public void setTlsEnabled(boolean tlsEnabled) {
        isTlsEnabled = tlsEnabled;
    }

    public boolean isTlsRequired() {
        return isTlsRequired;
    }

    public void setTlsRequired(boolean tlsRequired) {
        isTlsRequired = tlsRequired;
    }

    public boolean isDebugEnabled() {
        return isDebugEnabled;
    }

    public void setDebugEnabled(boolean debugEnabled) {
        isDebugEnabled = debugEnabled;
    }
}
