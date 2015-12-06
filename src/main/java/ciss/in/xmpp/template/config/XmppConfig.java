/**
 * Copyright 2015 Kamran Zafar
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package ciss.in.xmpp.template.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by kamran on 05/08/15.
 */

@Component
@ConfigurationProperties(locations = "classpath:xmpp.properties", prefix = "xmpp")
public class XmppConfig {
	
	private String domainHost;
	private String fusekiHost;
	private String kafkaHost;
	private String zookeeperHost;
	private String zookeeperPort;
	
    private String host;
    
    private int port;
    
    private String httpBind;
    
    private String prebindUrl;
    
    private String admin;
    private String adminPassword;
    private int listenPost;
    
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public String getHttpBind() {
		return httpBind;
	}
	public void setHttpBind(String httpBind) {
		this.httpBind = httpBind;
	}
	public String getPrebindUrl() {
		return prebindUrl;
	}
	public void setPrebindUrl(String prebindUrl) {
		this.prebindUrl = prebindUrl;
	}
	public String getAdmin() {
		return admin;
	}
	public void setAdmin(String admin) {
		this.admin = admin;
	}
	public String getAdminPassword() {
		return adminPassword;
	}
	public void setAdminPassword(String adminPassword) {
		this.adminPassword = adminPassword;
	}
	public int getListenPost() {
		return listenPost;
	}
	public void setListenPost(int listenPost) {
		this.listenPost = listenPost;
	}
	public String getDomainHost() {
		return domainHost;
	}
	public void setDomainHost(String domainHost) {
		this.domainHost = domainHost;
	}
	public String getFusekiHost() {
		return fusekiHost;
	}
	public void setFusekiHost(String fusekiHost) {
		this.fusekiHost = fusekiHost;
	}
	public String getKafkaHost() {
		return kafkaHost;
	}
	public void setKafkaHost(String kafkaHost) {
		this.kafkaHost = kafkaHost;
	}
	public String getZookeeperHost() {
		return zookeeperHost;
	}
	public void setZookeeperHost(String zookeeperHost) {
		this.zookeeperHost = zookeeperHost;
	}
	public String getZookeeperPort() {
		return zookeeperPort;
	}
	public void setZookeeperPort(String zookeeperPort) {
		this.zookeeperPort = zookeeperPort;
	}
}