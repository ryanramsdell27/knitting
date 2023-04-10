package dev.ryanramsdell.io;

import dev.ryanramsdell.data.Edge;
import dev.ryanramsdell.data.KnittingPattern;
import dev.ryanramsdell.data.Point;
import dev.ryanramsdell.data.Stitch;

import java.util.ArrayList;
import java.util.StringJoiner;

public class Blender {

    private static final String[] header = {"import bpy", "vertices=[]", "edges=[]", "faces=[]"};

    public static String toBlenderApi(KnittingPattern pattern) {
        StringJoiner program = new StringJoiner("\n");
        for(String line : header) {
            program.add(line);
        }

        int numLive = pattern.getNumCastOn();
        double stitchGuage = 1.0;
        double r = stitchGuage / (2 * Math.sin(Math.PI / numLive));

        // Read cast on stitches
        ArrayList<Stitch> liveStitches = new ArrayList<>();
        Stitch current = pattern.getStart();

        for(int i = 0; i < numLive; i++) {
            liveStitches.add(current);

            double d = 2 * Math.PI * i / numLive;
            double x = r * Math.cos(d);
            double y = r * Math.sin(d);
            current.setVertex(new Point(x, y, 0));
            current = current.getSuccessor();
        }

        // Read remaining stitches
        while(current != null) {
            Point c = new Point(0,0,0);
            for(Stitch parent : current.getParents()) {
                c.add(parent.getVertex());
            }
            c.scale(1.0/current.getParents().size());
            c.z += stitchGuage;
            current.setVertex(c);
            current = current.getSuccessor();
        }

        ArrayList<Point> vertices = new ArrayList<>();
        ArrayList<Edge> edges = new ArrayList<>();
        for(Stitch stitch : pattern.getStitches().stream().sorted(Stitch.getComparator()).toList()) {
            vertices.add(stitch.getOrderId(), stitch.getVertex());
            edges.addAll(Edge.generateEdges(stitch));
        }

        program.add(String.format("vertices=%s", vertices));
        program.add(String.format("edges=%s", edges));
        program.add("new_mesh = bpy.data.meshes.new('new_mesh')");
        program.add("new_mesh.from_pydata(vertices, edges, faces)");
        program.add("new_mesh.update()");
        program.add("new_object = bpy.data.objects.new('new_object', new_mesh)");
        program.add("new_collection = bpy.data.collections.new('new_collection')");
        program.add("bpy.context.scene.collection.children.link(new_collection)");
        program.add("new_collection.objects.link(new_object)");
        return program.toString();
    }
}
