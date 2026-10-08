package com.abm.mainet.cfc.challan.service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.web.context.request.RequestAttributes;

/**
 * Minimal RequestAttributes implementation that lets session-scoped Spring
 * beans (e.g. UserSession) be resolved on a non-HTTP thread, such as a
 * {@code @Scheduled} job's worker thread.
 * <p>
 * Backed by a plain map instead of a real HttpSession — intended to live for
 * exactly the duration of one scheduled run on one thread. Bind it via
 * {@code RequestContextHolder.setRequestAttributes(...)} before invoking any
 * code that depends on session-scoped beans, and always clear it afterwards via
 * {@code RequestContextHolder.resetRequestAttributes()} in a finally block so
 * state doesn't leak to the next task on the same pooled thread.
 */

/**
 * @author AnwarulHasan
 * @since 29/07/2027
 */

public class ThreadBoundRequestAttributes implements RequestAttributes {

	private final Map<String, Object> attributes = new ConcurrentHashMap<>();
	private final String sessionId;

	/**
	 * @param sessionId an identifier for this bound context — used only as the
	 *                  value returned by {@link #getSessionId()}, has no effect on
	 *                  storage or lookup. Pass something traceable back to the run
	 *                  that created it, e.g.
	 *                  "scheduler-&lt;jobName&gt;-&lt;threadId&gt;", so it's
	 *                  identifiable in logs if ever inspected.
	 */
	public ThreadBoundRequestAttributes(String sessionId) {
		this.sessionId = sessionId;
	}

	@Override
	public Object getAttribute(String name, int scope) {
		return attributes.get(key(name, scope));
	}

	@Override
	public void setAttribute(String name, Object value, int scope) {
		attributes.put(key(name, scope), value);
	}

	@Override
	public void removeAttribute(String name, int scope) {
		attributes.remove(key(name, scope));
	}

	@Override
	public String[] getAttributeNames(int scope) {
		String prefix = scope + ":";
		return attributes.keySet().stream().filter(k -> k.startsWith(prefix)).map(k -> k.substring(prefix.length()))
				.toArray(String[]::new);
	}

	@Override
	public void registerDestructionCallback(String name, Runnable callback, int scope) {
		// No-op: this whole attribute set is discarded when the scheduled run
		// finishes, so per-bean destruction callbacks aren't needed here.
	}

	@Override
	public Object resolveReference(String key) {
		return null;
	}

	@Override
	public String getSessionId() {
		return sessionId;
	}

	@Override
	public Object getSessionMutex() {
		return this;
	}

	private String key(String name, int scope) {
		return scope + ":" + name;
	}
}