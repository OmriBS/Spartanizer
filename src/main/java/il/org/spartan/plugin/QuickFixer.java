package il.org.spartan.plugin;

import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.*;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.ui.*;

/** A quickfix generator for spartanization refactoring
 * @author Boris van Sosin <code><boris.van.sosin [at] gmail.com></code>
 * @since 2013/07/01 */
public final class QuickFixer implements IMarkerResolutionGenerator {
  public static IMarkerResolution disableClassFix() {
    return GUI$Applicator.getToggle(SuppressWarningsLaconicOnOff.Type.CLASS, "Disable spartanization for class");
  }

  public static IMarkerResolution disableFileFix() {
    return GUI$Applicator.getToggle(SuppressWarningsLaconicOnOff.Type.FILE, "Disable spartanization for file");
  }

  public static IMarkerResolution disableFunctionFix() {
    return GUI$Applicator.getToggle(SuppressWarningsLaconicOnOff.Type.FUNCTION, "Disable spartanization for scope");
  }

  @Override public IMarkerResolution[] getResolutions(final IMarker m) {
    try {
      final GUI$Applicator $ = Tips.get((String) m.getAttribute(Builder.SPARTANIZATION_TYPE_KEY));
      assert $ != null;
      return new IMarkerResolution[] { $.getFix(), $.getFixWithPreview(),
          new LaconizeSelection.Enclosure(MethodDeclaration.class, "Laconize entire function"),
          new LaconizeSelection.Enclosure(TypeDeclaration.class, "Laconize entire class"), GUI$Applicator.getTipperCommitDeclaration(),
          GUI$Applicator.getWringCommitFile(), GUI$Applicator.getWringCommitProject(), QuickFixer.disableFunctionFix(), QuickFixer.disableClassFix(),
          QuickFixer.disableFileFix() };
    } catch (final CoreException x) {
      LoggingManner.logEvaluationError(this, x);
      return new IMarkerResolution[] {};
    }
  }
}