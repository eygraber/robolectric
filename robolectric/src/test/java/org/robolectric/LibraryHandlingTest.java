package org.robolectric;

import android.content.res.Resources;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.annotation.Config;

import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(TestRunners.WithDefaults.class)
public class LibraryHandlingTest {
  private final Resources resources = Robolectric.application.getResources();

  @Ignore("this seems to be unsupported behavior after Android's library scheme changes") @Test
  public void shouldFetchResourcesFromExplicitlyIndicatedLibrary() throws Exception {
    assertEquals("from main", resources.getText(org.robolectric.R.string.only_in_main));
    assertEquals("from lib1", resources.getText(org.robolectric.lib1.R.string.only_in_lib1));
    assertEquals("from lib2", resources.getText(org.robolectric.lib2.R.string.only_in_lib2));
    assertEquals("from lib3", resources.getText(org.robolectric.lib3.R.string.only_in_lib3));
  }

  @Test
  public void shouldFetchResourcesFromMergedLibraries() throws Exception {
    assertEquals("from main", resources.getText(org.robolectric.R.string.only_in_main));
    assertEquals("from lib1", resources.getText(org.robolectric.R.string.only_in_lib1));
    assertEquals("from lib2", resources.getText(org.robolectric.R.string.only_in_lib2));
    assertEquals("from lib3", resources.getText(org.robolectric.R.string.only_in_lib3));
  }

  @Test
  public void shouldFetchResourcesAccordingToLibraryPrecedence() throws Exception {
    // main includes lib1 and lib2; lib1 includes lib3
    assertEquals("from main", resources.getText(org.robolectric.R.string.in_all_libs));
    assertEquals("from lib3", resources.getText(org.robolectric.R.string.in_lib2_and_lib3));
    assertEquals("from lib1", resources.getText(org.robolectric.R.string.in_lib1_and_lib3));
    assertEquals("from main", resources.getText(org.robolectric.R.string.in_main_and_lib1));
  }

  @Test
  @Config(manifest="src/test/resources/TestAndroidManifest.xml", libraries="lib1")
  public void libraryConfigShouldOverrideProjectProperties() throws Exception {
    AndroidManifest manifest = Robolectric.shadowOf(Robolectric.application).getAppManifest();
    List<AndroidManifest> libraryManifests = manifest.getLibraryManifests();
    assertEquals(1, libraryManifests.size());
    assertEquals("org.robolectric.lib1", libraryManifests.get(0).getPackageName());
  }
}
