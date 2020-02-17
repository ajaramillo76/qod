package edu.cnm.deepdive.qod.view;

import java.util.Date;
import java.util.UUID;
import org.springframework.lang.NonNull;

public interface FlatQuote {

  @NonNull
  UUID getId();

  @NonNull
  Date getCreated();

  @NonNull
  Date getUpdated();

  @NonNull
  String getText();

}
