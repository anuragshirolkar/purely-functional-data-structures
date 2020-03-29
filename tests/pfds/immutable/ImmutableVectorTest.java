package pfds.immutable;

import static org.junit.Assert.assertEquals;

import java.util.Optional;

import org.junit.Test;

public class ImmutableVectorTest {

  @Test
  public void testEmpty() throws Exception {
    ImmutableVector<Integer> empty = ImmutableVector.of();

    assertEquals(empty.size(), 0);
    assertEquals(empty.append(10).size(), 1);
    assertEquals(empty.append(10).get(0), Integer.valueOf(10));
  }

  @Test
  public void testNonEmpty() throws Exception {
    ImmutableVector<Integer> big = ImmutableVector.of();
    for (int i = 0; i < 1000; i++) {
        big = big.append(i);
    }
    
    assertEquals(big.get(10), Integer.valueOf(10));
    assertEquals(big.size(), 1000);
    assertEquals(big.append(1000).size(), 1001);
    assertEquals(big.set(10,12345).get(10), Integer.valueOf(12345));
    assertEquals(big.stream().reduce(Integer::sum), Optional.of(Integer.valueOf(999*1000/2)));
  }
}