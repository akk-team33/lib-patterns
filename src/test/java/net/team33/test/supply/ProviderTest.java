package net.team33.test.supply;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

import net.team33.libs.supply.Provider;
import org.junit.Test;


public class ProviderTest
{

  private static final int MAX = 16;

  private final AtomicInteger nextInt = new AtomicInteger(0);
  private final Provider<Integer> provider = new Provider<>(nextInt::incrementAndGet);

  @Test
  public void provide()
  {
    for ( int count = MAX; count > 0; count-- ) {
      final Integer subject = provider.provide();
      try {
        assertEquals(1, subject.intValue());
      } finally {
        provider.restore(subject);
      }
    }
  }

  @Test
  public void run() throws InterruptedException
  {
    final Queue<Integer> results = new ConcurrentLinkedQueue<>();
    final List<Thread> threads = new ArrayList<>(MAX);
    for ( int count = MAX; count > 0; count-- )
    {
      threads.add(new Thread(() -> {
        provider.run(e -> {
          try
          {
            results.add(e);
            Thread.sleep(10);
          }
          catch (InterruptedException ex)
          {
            throw new UnsupportedOperationException("not yet implemented", ex);
          }
        });
      }));
    }
    for ( Thread thread : threads )
    {
      thread.start();
    }
    for ( Thread thread : threads )
    {
      thread.join();
    }
    assertEquals(MAX, results.size());
    assertTrue(1 < results.stream().reduce(0, Math::max));
  }

  @Test
  public void get()
  {
    for ( int i = 0; i < MAX; ++i )
    {
      assertEquals(Integer.valueOf(1), provider.get(j -> j));
    }
  }
}
