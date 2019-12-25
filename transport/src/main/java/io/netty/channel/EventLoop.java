/*
 * Copyright 2012 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package io.netty.channel;

import io.netty.util.concurrent.OrderedEventExecutor;

/**
 * Will handle all the I/O operations for a {@link Channel} once registered.
 *
 * One {@link EventLoop} instance will usually handle more than one {@link Channel} but this may depend on
 * implementation details and internals.
 *
 * 将会处理所有的IO操作一旦注册完成。
 * 一个EventLoop实例通常会处理多个Channel的io操作，但这也要依赖于具体的实现细节。
 *
 *
 * EventLoop最重要的方法是register()方法, 但是注意这个方法是在EventLoopGroup中定义.
 *
 */
public interface EventLoop extends OrderedEventExecutor, EventLoopGroup {

    // // 返回所属的EventLoopGroup
    @Override
    EventLoopGroup parent();
}
