import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import java.util.*;

public class Main extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	public Main() {
	}

	Circle currNodeMoved;
	Circle currNodeFrom;
	Circle currNodeTo;
	Line currEdgeTo;
	boolean bTMP = false;
	public static int N;
	public static boolean connected;
	public static boolean eulerian;
	public static boolean tough;
	public static boolean hamiltonian;
	private Scene sc;
	private RadioButton edgeDrawingButton;
	private RadioButton vertexPlacingButton;
	private RadioButton vertexMovingButton;
	private Button runAlg;
	private Button clear;
	private VBox buttons = new VBox();
	private Group root;
	private BorderPane pane;
	ArrayList<ArrayList<Integer>> graph = new ArrayList<ArrayList<Integer>>();
	ArrayList<Circle> vertices = new ArrayList<Circle>();
	ArrayList<ArrayList<Line>> edgeMatrix = new ArrayList<ArrayList<Line>>();


	@Override
	public void start(Stage stage) {
		pane = new BorderPane();
		root = new Group();
		sc = new Scene(pane, 1000, 600);
		stage.setScene(sc);
		edgeDrawingButton = new RadioButton("Edge Drawing/Deleting Mode");
		vertexPlacingButton = new RadioButton("Vertex Placing/Deleting Mode");
		vertexMovingButton = new RadioButton("Vertex Moving Mode");
		runAlg = new Button("Check if this is a Harris Graph");
		clear = new Button("Clear Graph");
		edgeDrawingButton.setOnAction(new buttonHandler());
		vertexPlacingButton.setOnAction(new buttonHandler());
		vertexMovingButton.setOnAction(new buttonHandler());
		runAlg.setOnAction(new buttonHandler());
		clear.setOnAction(new buttonHandler());
		pane.setOnMouseMoved(new mouseHandler());
		pane.setOnMouseClicked(new mouseHandler());
		pane.setOnMousePressed(new mouseHandler());
		pane.setOnMouseReleased(new mouseHandler());
		pane.setOnMouseDragged(new mouseHandler());
		pane.getChildren().add(root);
		buttons.getChildren().addAll(edgeDrawingButton, vertexPlacingButton, vertexMovingButton, runAlg, clear);
		stage.show();

		Stage buttonsScreen = new Stage();
		Scene buttonsScene = new Scene(buttons, 300, 200);
		buttonsScreen.setScene(buttonsScene);
		buttonsScreen.show();
	}

	private class buttonHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event) {
			if (event.getSource() == edgeDrawingButton) {
				vertexPlacingButton.setSelected(false);
				vertexMovingButton.setSelected(false);
			} else if (event.getSource() == vertexPlacingButton) {
				vertexMovingButton.setSelected(false);
				edgeDrawingButton.setSelected(false);
			} else if (event.getSource() == vertexMovingButton) {
				vertexPlacingButton.setSelected(false);
				edgeDrawingButton.setSelected(false);
			} else if (event.getSource() == runAlg) {
				try {
					checkHarrisGraph(graph);
				} catch (IndexOutOfBoundsException err) {

				}
			} else if (event.getSource() == clear) {
				graph = new ArrayList<ArrayList<Integer>>();
				for (int i = 0; i < vertices.size(); i++) {
					root.getChildren().remove(vertices.get(i));
				}
				vertices = new ArrayList<Circle>();
				for (int i = 0; i < edgeMatrix.size(); i++) {
					for (int j = 0; j < edgeMatrix.size(); j++) {
						if (edgeMatrix.get(i).get(j) != null) {
							root.getChildren().remove(edgeMatrix.get(i).get(j));
						}
					}
				}
				edgeMatrix = new ArrayList<ArrayList<Line>>();
			}
		}

	}

	private class mouseHandler implements EventHandler<MouseEvent> {
		@Override
		public void handle(MouseEvent event) {
			if (vertexPlacingButton.isSelected()) {
				if (event.getEventType().equals(MouseEvent.MOUSE_CLICKED) && event.getButton() == MouseButton.PRIMARY) {
					Circle node = new Circle(10);
					node.setOnMouseMoved(new mouseHandler());
					node.setOnMouseClicked(new mouseHandler());
					node.setOnMousePressed(new mouseHandler());
					node.setOnMouseReleased(new mouseHandler());
					node.setOnMouseDragged(new mouseHandler());

					node.setCenterX(event.getX());
					node.setCenterY(event.getY());
					root.getChildren().add(node);
					vertices.add(node);
					graph.add(new ArrayList<Integer>());
					edgeMatrix.add(new ArrayList<Line>());
					for (int i = 0; i < edgeMatrix.size(); i++) {
						edgeMatrix.get(i).add(null);
					}
					while (edgeMatrix.get(graph.size() - 1).size() < graph.size()) {
						edgeMatrix.get(graph.size() - 1).add(null);
					}
				} else if (event.getEventType().equals(MouseEvent.MOUSE_CLICKED)&& event.getButton() == MouseButton.SECONDARY && event.getSource() instanceof Circle) {
					int i = vertices.indexOf(event.getSource());

					root.getChildren().remove(vertices.get(i));
					for (int k = 0; k < graph.size(); k++) {
						for (int j = 0; j < graph.get(k).size(); j++) {
							if (graph.get(k).get(j) == i) {
								graph.get(k).remove(j);
								j--;
							} else if (graph.get(k).get(j) > i) {
								graph.get(k).set(j, graph.get(k).get(j) - 1);

							}
						}
					}
					graph.remove(i);
					vertices.remove(i);
					for (int j = 0; j < edgeMatrix.size(); j++) {
						for (int k = 0; k < edgeMatrix.get(j).size(); k++) {
							if (edgeMatrix.get(j).get(k) != null
									&& root.getChildren().contains(edgeMatrix.get(j).get(k))) {
								root.getChildren().remove(edgeMatrix.get(j).get(k));
							}
						}
					}
					edgeMatrix = new ArrayList<ArrayList<Line>>();
					for (int j = 0; j < graph.size(); j++) {
						edgeMatrix.add(new ArrayList<Line>());
						for (int k = 0; k < graph.size(); k++) {
							edgeMatrix.get(j).add(null);
						}
					}
					for (int j = 0; j < graph.size(); j++) {
						for (int k = 0; k < graph.get(j).size(); k++) {
							Line l1 = new Line(vertices.get(j).getCenterX(), vertices.get(j).getCenterY(),
									vertices.get(graph.get(j).get(k)).getCenterX(),
									vertices.get(graph.get(j).get(k)).getCenterY());
							l1.setOnMouseMoved(new mouseHandler());
							l1.setOnMouseClicked(new mouseHandler());
							l1.setOnMousePressed(new mouseHandler());
							l1.setOnMouseReleased(new mouseHandler());
							l1.setOnMouseDragged(new mouseHandler());
							l1.setStrokeWidth(5);
							edgeMatrix.get(j).set(graph.get(j).get(k), l1);
						}
					}
					for (int j = 0; j < edgeMatrix.size(); j++) {
						for (int k = 0; k < edgeMatrix.size(); k++) {
							if (edgeMatrix.get(j).get(k) != null
									&& !root.getChildren().contains(edgeMatrix.get(j).get(k))) {
								root.getChildren().add(edgeMatrix.get(j).get(k));
							}
						}
					}
				}

			} else if (vertexMovingButton.isSelected()) {
				if (event.getEventType().equals(MouseEvent.MOUSE_PRESSED)) {
					for (int i = 0; i < root.getChildren().size(); i++) {
						if (root.getChildren().get(i).getLayoutBounds().contains(event.getX(), event.getY())
								&& root.getChildren().get(i) instanceof Circle) {
							currNodeMoved = (Circle) root.getChildren().get(i);
							break;
						}
					}
				}
				if (event.getEventType().equals(MouseEvent.MOUSE_DRAGGED)) {
					if (currNodeMoved != null) {
						currNodeMoved.setCenterX(event.getX());
						currNodeMoved.setCenterY(event.getY());
						int nodeID = vertices.indexOf(currNodeMoved);
						for (int i = 0; i < edgeMatrix.get(nodeID).size(); i++) {
							if (edgeMatrix.get(nodeID).get(i) != null) {
								edgeMatrix.get(nodeID).get(i).setEndX(event.getX());
								edgeMatrix.get(nodeID).get(i).setEndY(event.getY());
								edgeMatrix.get(nodeID).get(i).setStartX(vertices.get(i).getCenterX());
								edgeMatrix.get(nodeID).get(i).setStartY(vertices.get(i).getCenterY());
								edgeMatrix.get(i).get(nodeID).setEndX(event.getX());
								edgeMatrix.get(i).get(nodeID).setEndY(event.getY());
								edgeMatrix.get(i).get(nodeID).setStartX(vertices.get(i).getCenterX());
								edgeMatrix.get(i).get(nodeID).setStartY(vertices.get(i).getCenterY());
							}

						}
					}
				}
				if (event.getEventType().equals(MouseEvent.MOUSE_RELEASED)) {
					currNodeMoved = null;
				}
			} else if (edgeDrawingButton.isSelected()) {
				if (event.getButton() == MouseButton.PRIMARY) {
					if (event.getEventType().equals(MouseEvent.MOUSE_PRESSED)) {
						bTMP = false;
						for (int i = 0; i < vertices.size(); i++) {
							if (vertices.get(i).getLayoutBounds().contains(event.getX(), event.getY())) {
								currNodeFrom = vertices.get(i);
								break;
							}
						}
					}
					if (event.getEventType().equals(MouseEvent.MOUSE_DRAGGED)) {
						if (currNodeFrom != null) {

							if (!bTMP) {
								currEdgeTo = new Line(currNodeFrom.getCenterX(), currNodeFrom.getCenterY(),
										event.getX(), event.getY());
								currEdgeTo.setStrokeWidth(5);
								root.getChildren().add(currEdgeTo);
								bTMP = true;
							}
							currEdgeTo.setStartX(currNodeFrom.getCenterX());
							currEdgeTo.setStartY(currNodeFrom.getCenterY());
							currEdgeTo.setEndX(event.getX());
							currEdgeTo.setEndY(event.getY());
						}
					}
					if (event.getEventType().equals(MouseEvent.MOUSE_RELEASED)) {
						for (int i = 0; i < vertices.size(); i++) {
							if (vertices.get(i).getLayoutBounds().contains(event.getX(), event.getY())) {
								if (currNodeFrom != vertices.get(i) && currNodeFrom != null) {
									currNodeTo = vertices.get(i);
									if (currEdgeTo != null && root.getChildren().contains(currEdgeTo)) {
										root.getChildren().remove(currEdgeTo);
									}
									Line edge = new Line(currNodeFrom.getCenterX(), currNodeFrom.getCenterY(),
											currNodeTo.getCenterX(), currNodeTo.getCenterY());
									edge.setOnMouseMoved(new mouseHandler());
									edge.setOnMouseClicked(new mouseHandler());
									edge.setOnMousePressed(new mouseHandler());
									edge.setOnMouseReleased(new mouseHandler());
									edge.setOnMouseDragged(new mouseHandler());

									edge.setStrokeWidth(5);
									root.getChildren().add(edge);
									graph.get(vertices.indexOf(currNodeFrom)).add(vertices.indexOf(currNodeTo));
									graph.get(vertices.indexOf(currNodeTo)).add(vertices.indexOf(currNodeFrom));
									edgeMatrix.get(vertices.indexOf(currNodeTo)).set(vertices.indexOf(currNodeFrom),
											edge);
									edgeMatrix.get(vertices.indexOf(currNodeFrom)).set(vertices.indexOf(currNodeTo),
											edge);
									break;
								}
							}
						}
						if (currEdgeTo != null && root.getChildren().contains(currEdgeTo)) {
							root.getChildren().remove(currEdgeTo);
						}
						currEdgeTo = null;
						currNodeFrom = null;
						currNodeTo = null;
					}
				} else if (event.getEventType().equals(MouseEvent.MOUSE_CLICKED)
						&& event.getButton() == MouseButton.SECONDARY && event.getSource() instanceof Line) {
					Line l = (Line) event.getSource();
					root.getChildren().remove(event.getSource());
					for (int i = 0; i < graph.size(); i++) {
						for (int j = 0; j < graph.size(); j++) {
							if (edgeMatrix.get(i).get(j) == l) {
								edgeMatrix.get(i).set(j, null);
								edgeMatrix.get(j).set(i, null);
								graph.get(i).remove(graph.get(i).indexOf(j));
								graph.get(j).remove(graph.get(j).indexOf(i));
								return;
							}
						}
					}
				}
			} else {

			}
		}

	}

	public void checkHarrisGraph(ArrayList<ArrayList<Integer>> graph) {
		N = graph.size();
		ArrayList<Integer> vecTMP = new ArrayList<Integer>();
		findAllPossibleVisitedPoints(0, graph, vecTMP, new ArrayList<Integer>());
		if (vecTMP.size() == graph.size()) {
			connected = true;
			System.out.println("Graph is connected");
		} else {
			connected = false;
			System.out.println("Graph isn't connected");
			System.out.println("This isn't a Harris Graph");
			return;
		}

		eulerian = true;
		checkEulerian(graph);
		if (eulerian) {
			System.out.println("Graph is Eulerian");
		} else {
			System.out.println("Graph isn't Eulerian");
			System.out.println("This isn't a Harris Graph");
			return;
		}
		hamiltonian = false;
		checkHamiltonian(0, new ArrayList<Integer>(), graph, 0);
		if (!hamiltonian) {
			System.out.println("Graph isn't Hamiltonian");
		} else {
			System.out.println("Graph is Hamiltonian");
			System.out.println("This isn't a Harris Graph");
			return;
		}
		tough = true;
		checkToughness(graph, new ArrayList<Integer>(), 0);
		if (tough) {
			System.out.println("Graph is Tough");
		} else {
			System.out.println("Graph isn't Tough");
			System.out.println("This isn't a Harris Graph");
			return;
		}
		System.out.println("This is a Harris Graph!");
	}

	// checks if every vertex has an even degree
	public static void checkEulerian(ArrayList<ArrayList<Integer>> graph) {
		for (int i = 0; i < graph.size(); i++) {
			if (graph.get(i).size() % 2 != 0) {
				eulerian = false;
				break;
			}
		}
	}

	// DFS algorithm that searches for a hamiltonian cycle
	public static void checkHamiltonian(int vertex, ArrayList<Integer> points_visited,
			ArrayList<ArrayList<Integer>> graph, int target) {
		if (vertex == target && (points_visited.size() == graph.size() || graph.size() == 1)) {
			hamiltonian = true;
			return;
		}
		for (int i = 0; i < points_visited.size(); i++) {
			if (points_visited.get(i) == vertex) {
				return;
			}
		}
		for (int i = 0; i < graph.get(vertex).size(); i++) {
			ArrayList<Integer> tmp = new ArrayList<Integer>();
			for (int j = 0; j < points_visited.size(); j++) {
				tmp.add(points_visited.get(j));
			}
			tmp.add(vertex);
			checkHamiltonian(graph.get(vertex).get(i), tmp, graph, target);
			tmp = null;
		}
	}

	// recursive algorithm that checks if every possible combination of vertices
	// removed results in more components of the graph created
	// NOTE: Can make faster if you only let a combination of at most ceiling(N/2)
	// vertices be removed when checked for G/S
	public static void checkToughness(ArrayList<ArrayList<Integer>> graph, ArrayList<Integer> set, int num) {
		if (graph.size() == num) {
			int comps = getNumComponents(graph, new ArrayList<ArrayList<Integer>>(), set);
			if (comps > set.size() && set.size() > 0) {
				tough = false;
				return;
			}
		} else {
			ArrayList<Integer> tmp1 = new ArrayList<Integer>();
			for (int i = 0; i < set.size(); i++) {
				tmp1.add(set.get(i));
			}
			tmp1.add(num);
			checkToughness(graph, tmp1, num + 1);
			tmp1 = null;
			ArrayList<Integer> tmp2 = new ArrayList<Integer>();
			for (int i = 0; i < set.size(); i++) {
				tmp2.add(set.get(i));
			}
			checkToughness(graph, tmp2, num + 1);
			tmp2 = null;
		}
	}

	// gives the number of components in a graph
	public static int getNumComponents(ArrayList<ArrayList<Integer>> graph, ArrayList<ArrayList<Integer>> pieces,
			ArrayList<Integer> set) {
		ArrayList<Integer> pointsLeft = new ArrayList<Integer>();
		for (int i = 0; i < N; i++) {
			boolean check = true;
			for (int k = 0; k < set.size(); k++) {
				if (set.get(k) == i) {
					check = false;
					break;
				}
			}
			if (check) {
				pointsLeft.add(i);
			}
		}
		while (pointsLeft.size() > 0) {
			ArrayList<Integer> group = new ArrayList<Integer>();
			findAllPossibleVisitedPoints(pointsLeft.get(0), graph, group, set);
			pieces.add(group);
			for (int i = 0; i < group.size(); i++) {
				for (int j = 0; j < pointsLeft.size(); j++) {
					if (group.get(i) == pointsLeft.get(j)) {
						pointsLeft.remove(j);
						j--;
					}
				}
			}
			group = null;
		}
		pointsLeft = null;
		return pieces.size();
	}

	// finds all possible points reachable from one given starting point - helper
	// method for getNumComponents()
	public static void findAllPossibleVisitedPoints(int vertex, ArrayList<ArrayList<Integer>> graph,
			ArrayList<Integer> visited, ArrayList<Integer> set) {
		for (int i = 0; i < visited.size(); i++) {
			if (vertex == visited.get(i)) {
				return;
			}
		}
		visited.add(vertex);
		for (int i = 0; i < graph.get(vertex).size(); i++) {
			boolean inSet = false;
			for (int j = 0; j < set.size(); j++) {
				if (graph.get(vertex).get(i) == set.get(j)) {
					inSet = true;
					break;
				}
			}
			if (!inSet) {
				findAllPossibleVisitedPoints(graph.get(vertex).get(i), graph, visited, set);
			}
		}
	}

}
