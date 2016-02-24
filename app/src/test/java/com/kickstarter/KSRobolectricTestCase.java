package com.kickstarter;

import android.content.Context;
import android.support.annotation.NonNull;

import com.kickstarter.libs.Environment;
import com.kickstarter.libs.KSString;
import com.kickstarter.libs.Koala;
import com.kickstarter.libs.MockTrackingClient;
import com.kickstarter.services.MockApiClient;
import com.kickstarter.services.MockWebClient;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.multidex.ShadowMultiDex;

import rx.observers.TestSubscriber;

@RunWith(KSRobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, shadows = ShadowMultiDex.class, sdk = KSRobolectricGradleTestRunner.DEFAULT_SDK)
public abstract class KSRobolectricTestCase extends TestCase {
  private TestKSApplication application;
  public TestSubscriber<String> koalaTest;
  private Environment environment;

  @Override
  @Before
  public void setUp() throws Exception {
    super.setUp();

    final MockTrackingClient testTrackingClient = new MockTrackingClient();
    koalaTest = new TestSubscriber<>();
    testTrackingClient.eventNames.subscribe(koalaTest);

    environment = application().component().environment().toBuilder()
      .apiClient(new MockApiClient())
      .webClient(new MockWebClient())
      .koala(new Koala(testTrackingClient))
      .build();
  }

  protected @NonNull TestKSApplication application() {
    if (application != null) {
      return application;
    }

    application = (TestKSApplication) RuntimeEnvironment.application;
    return application;
  }

  protected @NonNull Context context() {
    return application().getApplicationContext();
  }

  protected @NonNull Environment environment() {
    return environment;
  }

  protected @NonNull KSString ksString() {
    return new KSString(application().getPackageName(), application().getResources());
  }
}
