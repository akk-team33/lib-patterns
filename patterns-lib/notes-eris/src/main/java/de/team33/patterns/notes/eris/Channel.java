package de.team33.patterns.notes.eris;

import java.util.function.Consumer;

/**
 * Identifies channels of notifications and associates specific sorts of events with resulting message types.
 * A typical application scenario might look something like this:
 * <p>
 * In the context of a service or similar, events can occur about which interested participants may need to be
 * notified. For example, a property of the service may change. A {@link Channel} is defined for each relevant
 * event type in the context of the service and in this way the event type is linked to a message type.
 * Last but not least, the service is the origin of the messages to be sent. Each channel "knows" how the
 * service will deliver the message to be transmitted. Example:
 * <pre>
 * public class SampleService {
 *
 *     private Path path;
 *     private Instant timestamp;
 *
 *     // ...
 *
 *     public interface Channel&lt;M&gt; extends de.team33.patterns.notes.eris.Channel&lt;M&gt;, Function&lt;SampleService, M&gt; {
 *
 *         Channel&lt;Path&gt; NEW_PATH = service -&gt; service.path;
 *         Channel&lt;Instant&gt; NEW_TIMESTAMP = service -&gt; service.timestamp;
 *         Channel&lt;SampleService&gt; UPDATED = service -&gt; service;
 *     }
 * }
 * </pre>
 * This example defines a context-specific derivation of {@link Channel} that also "knows" a method to get a
 * corresponding message from the service.
 * In principle, this is not necessary, but it simplifies the structuring and formulation of the individual constants.
 *
 * @see Registry#add(Channel, Consumer) Registry.add(Channel&lt;M&gt;, Consumer&lt;? super M&gt;)
 * @see Audience#send(Channel, Object) Audience.send(Channel&lt;M&gt;, M)
 * @see Registry#add(Channel, Consumer)
 * @see Audience#send(Channel, Object)
 *
 * @param <M> The message type.
 */
public interface Channel<M> {
}
