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

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import ciss.in.xmpp.template.XmppUser;

/**
 * Created by kamran on 04/08/15.
 */
@RestController
@RequestMapping("/prebind")
public class XmppPrebindResource {

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public XmppUser prebind() {
        return (XmppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
