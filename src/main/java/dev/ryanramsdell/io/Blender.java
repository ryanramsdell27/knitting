package dev.ryanramsdell.io;

import dev.ryanramsdell.data.*;

import java.util.ArrayList;
import java.util.StringJoiner;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class Blender {

    private static final String[] header = {"import bpy", "vertices=[]", "edges=[]", "faces=[]"};

    public static String toBlenderApi(KnittingPattern pattern) {
        StringJoiner program = new StringJoiner("\n");
        for(String line : header) {
            program.add(line);
        }

        MeshData md = computeVerticesAndEdges(pattern);


        program.add(String.format("vertices=%s", md.getVertices()));
        program.add(String.format("edges=%s", md.getEdges()));
        program.add("new_mesh = bpy.data.meshes.new('new_mesh')");
        program.add("new_mesh.from_pydata(vertices, edges, faces)");
        program.add("new_mesh.update()");
        program.add("new_object = bpy.data.objects.new('new_object', new_mesh)");
        program.add("new_collection = bpy.data.collections.new('new_collection')");
        program.add("bpy.context.scene.collection.children.link(new_collection)");
        program.add("new_collection.objects.link(new_object)");
        return program.toString();
    }

    public static MeshData computeVerticesAndEdges(KnittingPattern pattern) {
        int numLive = pattern.getNumCastOn();
        double stitchGauge = 1.0;
        double r = stitchGauge / (2 * sin(Math.PI / numLive));
        Point dir = new Point(0, stitchGauge, stitchGauge);
        Point curr = new Point(0,0,0);

        // Read cast on stitches
        ArrayList<Stitch> liveStitches = new ArrayList<>();
        Stitch current = pattern.getStart();

        for(int i = 0; i < numLive; i++) {
            liveStitches.add(current);
            current.setVertex(new Point(curr.x, curr.y, 0));
            double angle = 2 * Math.PI / numLive;
            double x = (cos(angle) * dir.x) - (sin(angle) * dir.y);
            double y = (sin(angle) * dir.x) + (cos(angle) * dir.y);
            dir.x = x;
            dir.y = y;
            curr.x += x;
            curr.y += y;
            current = current.getSuccessor();
        }

        // Read remaining stitches
        while(current != null) {
            Point c = new Point(0,0,0);
            for(Stitch parent : current.getParents()) {
                c.add(parent.getVertex());
            }
            c.scale(1.0/current.getParents().size());
            c.z += stitchGauge;
            current.setVertex(new Point(curr.x, curr.y, c.z));

            numLive -= current.getParents().size() - 1;

            double angle = 2 * Math.PI / numLive;
            double x = (cos(angle) * dir.x) - (sin(angle) * dir.y);
            double y = (sin(angle) * dir.x) + (cos(angle) * dir.y);
            dir.x = x;
            dir.y = y;
            curr.x += x;
            curr.y += y;

            current = current.getSuccessor();
        }

        ArrayList<Point> vertices = new ArrayList<>();
        ArrayList<Edge> edges = new ArrayList<>();
        for(Stitch stitch : pattern.getStitches().stream().sorted(Stitch.getComparator()).toList()) {
            vertices.add(stitch.getOrderId(), stitch.getVertex());
            edges.addAll(Edge.generateEdges(stitch));
        }
        return new MeshData(vertices, edges);
    }
}
