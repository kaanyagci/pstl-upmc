package com.puck.menu;



import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.AbstractAction;
import javax.swing.JMenuItem;

import org.piccolo2d.PNode;
import org.piccolo2d.extras.pswing.PSwingCanvas;

import com.puck.arrows.ArrowNodesHolder;
import com.puck.arrows.ParrowUses;
import com.puck.nodes.piccolo2d.Edge;
import com.puck.nodes.piccolo2d.Node;
import com.puck.nodes.piccolo2d.PiccoloCustomNode;
import com.puck.utilities.piccolo2d.XmlToStructure;

public class CreateUsesEdgesOf extends JMenuItem {
	private HashMap<String, PiccoloCustomNode> allPNodes;
	private Map<String, Node> m = new XmlToStructure().parseNode();
	private HashMap<String, Node> listNodes = new HashMap<>(m);
	private PiccoloCustomNode pnode;
	private PSwingCanvas canvas;
	private Menu menu;
	private ArrowNodesHolder ANH;

	public CreateUsesEdgesOf(PiccoloCustomNode pnode, PSwingCanvas canvas, HashMap<String, PiccoloCustomNode> allPNodes,
			Menu menu, ArrowNodesHolder ANH) {
		super();
		this.setText("show uses outgoing");
		this.allPNodes = allPNodes;
		this.pnode = pnode;
		this.canvas = canvas;
		this.menu = menu;
		this.ANH = ANH;
		addActionListener();
	}

	public void DrawEdges(PiccoloCustomNode target, PSwingCanvas canvas) {
		Node node = listNodes.get(target.getidNode());
		
		if ( node!= null && node.getRelation() !=  null) {
			HashMap<String, Edge> relation = node.getRelation();
			for (Entry<String, Edge> edgeEntry : relation.entrySet()) {
				Edge e = edgeEntry.getValue();
				if (e.getType().equals("uses")) {
					PNode from = target;
					PNode to = (allPNodes.get(e.getTo()));
					if (to.getParent() instanceof PiccoloCustomNode
							&& !((PiccoloCustomNode) to.getParent()).isHidden()) {
						ANH.addArrow(new ParrowUses(from, to, 10, from, to));
					} else {
						for (PiccoloCustomNode pnode : ((PiccoloCustomNode) to).getAscendency()) {
							if (!pnode.isHidden()) {
								ANH.addArrow(new ParrowUses(from, to, 10, from, pnode));
								break;
							}
						}
					}
				}
			}
		}
		this.menu.hideMenu();
	}

	public void addActionListener() {
		this.addActionListener(new AbstractAction() {

			public void actionPerformed(ActionEvent arg0) {
				DrawEdges(pnode, canvas);
			}
		});
	}
}