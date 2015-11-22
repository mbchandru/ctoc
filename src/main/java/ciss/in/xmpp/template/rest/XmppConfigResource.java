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

package ciss.in.xmpp.template.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import ciss.in.Application;
import ciss.in.xmpp.template.XmppUser;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Created by kamran on 05/08/15.
 */
@RestController
@RequestMapping("/config")
public class XmppConfigResource {

    public static final String PREBIND_URL = "prebindUrl";
    public static final String BIND_URL = "bindUrl";
    public static final String JID = "jid";

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> config(HttpServletRequest httpServletRequest) {
    	HttpSession session = httpServletRequest.getSession();
    	Object obj = session.getAttribute("xmppUser");
    	String jid = null;
       	if (obj instanceof XmppUser) {
       		jid = ((XmppUser) obj).getJid();
       	}
        Map<String, Object> config = new HashMap<String, Object>();
        config.put(PREBIND_URL, Application.xmppConfig.getPrebindUrl());
        config.put(BIND_URL, "http://" + Application.xmppConfig.getHost() + ":" + Application.xmppConfig.getPort() + Application.xmppConfig.getHttpBind());
        config.put(JID, jid);

        return config;
    }
}
