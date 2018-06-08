/*
 * Copyright 2014 Open mHealth
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.openmhealth.dsu.domain;


import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collections;

import static org.openmhealth.dsu.configuration.OAuth2Properties.END_USER_ROLE;


/**
 * A user's authentication details.
 *
 * @author Emerson Farrugia
 */
public class EndUserUserDetails extends User {

    public EndUserUserDetails(String username, String password) {
        super(username, password, Collections.singleton(new SimpleGrantedAuthority(END_USER_ROLE)));
    }
}
