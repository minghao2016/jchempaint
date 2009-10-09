package org.openscience.jchempaint;

import java.util.HashMap;
import java.util.Map;

import javax.vecmath.Point2d;

import org.fest.swing.applet.AppletViewer;
import org.fest.swing.fixture.FrameFixture;
import org.fest.swing.fixture.JPanelFixture;
import org.fest.swing.launcher.AppletLauncher;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IBond;
import org.openscience.cdk.interfaces.IChemModel;
import org.openscience.jchempaint.applet.JChemPaintEditorApplet;

/**
 * An abstract base class for applet tests. It sets up and tears down
 * and offers some convenience methods.
 */
public class AbstractAppletTest {
    private static AppletViewer viewer;
    protected static FrameFixture applet;
    protected static JChemPaintEditorApplet jcpApplet;


    @BeforeClass public static void setUp() {
        jcpApplet = new JChemPaintEditorApplet();
        Map<String, String> parameters = new HashMap<String, String>();
        viewer = AppletLauncher.applet(jcpApplet)
            .withParameters(parameters)
            .start();
        applet = new FrameFixture(viewer);
        applet.show();
    }
    
    protected Point2d getBondPoint(JChemPaintPanel panel, int bondnumber) {
        IBond bond = panel.getChemModel().getMoleculeSet().getAtomContainer(0).getBond(bondnumber);
        return panel.getRenderPanel().getRenderer().toScreenCoordinates((bond.getAtom(0).getPoint2d().x+bond.getAtom(1).getPoint2d().x)/2,(bond.getAtom(0).getPoint2d().y+bond.getAtom(1).getPoint2d().y)/2);
    }
    
    protected Point2d getAtomPoint(JChemPaintPanel panel, int atomnumber){
        IAtom atom = panel.getChemModel().getMoleculeSet().getAtomContainer(0).getAtom(atomnumber);
        return panel.getRenderPanel().getRenderer().toScreenCoordinates(atom.getPoint2d().x,atom.getPoint2d().y);
    }    
    
    protected void restoreModelToEmpty(){
        JPanelFixture jcppanel=applet.panel("appletframe");
        JChemPaintPanel panel = (JChemPaintPanel)jcppanel.target;
        IChemModel basic = DefaultChemObjectBuilder.getInstance().newChemModel();
        basic.setMoleculeSet(basic.getBuilder().newMoleculeSet());
        basic.getMoleculeSet().addAtomContainer(
                basic.getBuilder().newMolecule());
        panel.setChemModel(basic);
        panel.getRenderPanel().getRenderer().getRenderer2DModel().setZoomFactor(1);
        panel.getRenderPanel().getRenderer().getRenderer2DModel().setBondLength(10.4);
        panel.get2DHub().updateView();
    }
    

    @AfterClass public static void tearDown() {
      viewer.unloadApplet();
      applet.cleanUp();
    }
}