package uk.gov.justice.laa.crime.cfecrime.cma.response;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class MapResponse {
    private MapResponse(){
    }
    public static class TreeNode {
        private final Map<String, Object> data;
        private final Map<String, TreeNode> children;

        TreeNode() {
            data = new HashMap<>();
            children = new HashMap<>();
        }

        public Map<String, Object> getData() { return Collections.unmodifiableMap(data); }

        public Map<String, TreeNode> getChildren() { return Collections.unmodifiableMap(children); }

        void put(String name, Object object) {
            data.put(name, object);
        }

        boolean containsKey(String name) {
            return children.containsKey(name);
        }

        TreeNode get(String name) {
            return children.get(name);
        }

        void addChild(String item, TreeNode newMap) {
            children.put(item, newMap);
        }
    }

    public static TreeNode toMap(ValueList response) {
        TreeNode result = new TreeNode();

        var mapVisitor = new Visitor() {
            @Override
            public void visit(Iterable<String> context, String name, boolean value) {
                findTarget(context).put(name, value);
            }

            @Override
            public void visit(Iterable<String> context, String name, BigDecimal value) {
                findTarget(context).put(name, value);
            }

            @Override
            public void visit(Iterable<String> context, String name, String value) {
                findTarget(context).put(name, value);
            }

            private TreeNode findTarget(Iterable<String> context) {
                var target = result;

                for (var item: context) {
                    if (target.containsKey(item)) {
                        target = target.get(item);
                    } else {
                        var newMap = new TreeNode();
                        target.addChild(item, newMap);
                        target = newMap;
                    }
                }

                return target;
            }
        };

        response.visit(Collections.emptyList(), mapVisitor);

        return result;
    }
}
