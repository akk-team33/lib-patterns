package net.team33.test.supply;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import net.team33.libs.supply.Provider;
import org.junit.Test;


public class ProviderTest
{

  private final AtomicInteger nextInt = new AtomicInteger(0);
  private final Provider<Integer> provider = new Provider<>(nextInt::incrementAndGet);

  @Test
  public void run() throws InterruptedException
  {
    final List<Integer> results = new ArrayList<>(16);
    final List<Thread> threads = new ArrayList<>(16);
    for ( long count = 16; count > 0; count-- )
    {
      threads.add(new Thread(() -> {
        provider.run(results::add);
      }));
    }
    for ( Thread thread1 : threads )
    {
      thread1.start();
    }
    for ( Thread thread : threads )
    {
      thread.join();
    }
    assertEquals(16, results.size());
    assertTrue(1 < results.stream().reduce(0, Math::max));
  }

  @Test
  public void get()
  {
    for ( int i = 0; i < 16; ++i )
    {
      assertEquals(Integer.valueOf(1), provider.get(j -> j));
    }
  }
}
