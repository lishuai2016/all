package com.ls.io.bupt.reactor;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

public class SocketReadHandler implements Runnable {
	private SocketChannel socketChannel;

	public SocketReadHandler(Selector selector, SocketChannel socketChannel) throws IOException {
		this.socketChannel = socketChannel;
		socketChannel.configureBlocking(false);

		SelectionKey selectionKey = socketChannel.register(selector, 0);

		// 将SelectionKey绑定为本Handler 下一步有事件触发时，将调用本类的run方法。
		// 参看dispatch(SelectionKey key)
		selectionKey.attach(this);

		// 同时将SelectionKey标记为可读，以便读取。
		selectionKey.interestOps(SelectionKey.OP_READ);
		selector.wakeup();
	}

	/**
	 * 处理读取数据
	 */
	@Override
	public void run() {
		ByteBuffer inputBuffer = ByteBuffer.allocate(1024);
		inputBuffer.clear();
		try {
			socketChannel.read(inputBuffer);
			// 激活线程池 处理这些request
			// requestHandle(new Request(socket,btt));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
