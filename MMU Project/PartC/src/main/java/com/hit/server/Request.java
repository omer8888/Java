package com.hit.server;

import java.lang.String;
import java.util.Map;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Request<T> implements Serializable {

	private T body;
	private Map<String, String> headers; // example: <"action" | "update">

	public Request(Map<String, String> headers, T body) { // C'tor
		setBody(body);
		setHeaders(headers);
	}

	public Map<String, String> getHeaders() {
		return headers;
	}

	public void setHeaders(Map<String, String> headers) {
		this.headers = headers;
	}

	public T getBody() {
		return body;
	}

	public void setBody(T body) {
		this.body = body;
	}

	@Override
	public String toString() {
		return body.toString() + " " + headers.toString();
	}
}
