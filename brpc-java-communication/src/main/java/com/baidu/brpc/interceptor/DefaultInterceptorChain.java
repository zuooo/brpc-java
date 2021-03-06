/*
 * Copyright (c) 2019 Baidu, Inc. All Rights Reserved.
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

package com.baidu.brpc.interceptor;

import com.baidu.brpc.exceptions.RpcException;
import com.baidu.brpc.protocol.Request;
import com.baidu.brpc.protocol.Response;

import java.util.Iterator;
import java.util.List;

public class DefaultInterceptorChain implements InterceptorChain {
    private List<Interceptor> interceptors;
    private Iterator<Interceptor> iterator;

    public DefaultInterceptorChain(List<Interceptor> interceptors) {
        this.interceptors = interceptors;
        this.iterator = interceptors.iterator();
    }

    @Override
    public void intercept(Request request, Response response) throws RpcException {
        if (iterator.hasNext()) {
            Interceptor interceptor = iterator.next();
            boolean success = interceptor.handleRequest(request);
            if (success) {
                interceptor.aroundProcess(request, response, this);
                if (request.getCallback() == null) {
                    interceptor.handleResponse(response);
                }
            }
        }
    }
}
