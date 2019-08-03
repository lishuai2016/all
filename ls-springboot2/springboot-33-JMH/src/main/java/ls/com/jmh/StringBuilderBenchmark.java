package ls.com.jmh;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;

/**
 * @program: lishuai-notes
 * @author: lishuai
 * @create: 2018-12-23 19:07
 * 比较字符串直接相加和StringBuilder的效率
 */
@BenchmarkMode(Mode.Throughput)
@Warmup(iterations = 3)
@Measurement(iterations = 10, time = 5, timeUnit = TimeUnit.SECONDS)
@Threads(8)
@Fork(2)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class StringBuilderBenchmark {


public static void main(String[] args) throws RunnerException {
    Options options = new OptionsBuilder()
           .include(StringBuilderBenchmark.class.getSimpleName())
           .output("d:/Benchmark.log")
           .build();
   new Runner(options).run();
}

@Benchmark
public void testStringAdd() {
String a = "";
for (int i = 0; i < 10; i++) {
          a += i;
      }
      print(a);
   }

   @Benchmark
   public void testStringBuilderAdd() {
       StringBuilder sb = new StringBuilder();
       for (int i = 0; i < 10; i++) {
           sb.append(i);
       }
       print(sb.toString());
   }

  private void print(String a) {
   }
}
