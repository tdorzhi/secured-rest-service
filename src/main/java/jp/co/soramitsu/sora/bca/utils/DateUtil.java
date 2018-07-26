package jp.co.soramitsu.sora.bca.utils;

import java.sql.Date;
import java.time.Clock;
import java.time.Instant;
import java.time.ZonedDateTime;
import lombok.experimental.UtilityClass;

@UtilityClass
public class DateUtil {

  public java.util.Date nowPlusSeconds(int seconds) {
    if (seconds < 0) {
      throw new IllegalArgumentException("Seconds must be greater than or equals 0");
    }
    Clock preciseClock = Clock.systemUTC();
    Instant pointInTimeInFuture = ZonedDateTime.now(preciseClock).toInstant().plusSeconds(seconds);
    return Date.from(pointInTimeInFuture);
  }

}
