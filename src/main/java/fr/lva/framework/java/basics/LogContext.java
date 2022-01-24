package fr.lva.framework.java.basics;

import org.apache.logging.log4j.ThreadContext;

/**
 * Logging contexts.
 */
public enum LogContext {
	
        // -------------------------------------------------------------------------
        // Enum values
        // -------------------------------------------------------------------------
        
        /** The User id **/
        Id,
        /** The User email **/
        Mail,
        /** Event Processed **/
        Event;
        
    // -------------------------------------------------------------------------
    // Accessors
    // -------------------------------------------------------------------------
    
    /**
     * Returns the current value for this context.
     * 
     * @return the context current value or <code>null</code> if no value was set for the
     *         being-processed request.
     */
    public String get() {
        return ThreadContext.get(this.name());
    }
    
    /**
     * Sets the current value for this context.
     * 
     * @param value the new value for the context or <code>null</code> to remove the current value.
     */
    public void addValue(String value) {
        if (value != null) {
            ThreadContext.put(this.name(), value);
        } else {
            this.clear();
        }
    }
    
    public static void init(String userId, String email) {
        clearAll();
        Id.addValue(userId);
        Mail.addValue(email);
    }
    
    public static void addEvent(String event) {
    	Event.addValue(event);
    }
    
    /**
     * Clears this context, removing its current value, if any.
     */
    public void clear() {
        ThreadContext.remove(this.name());
    }
    
    /**
     * Clears all log contexts.
     */
    public static void clearAll() {
        ThreadContext.clearMap();
    }
    
}
