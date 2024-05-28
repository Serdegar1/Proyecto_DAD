//package es.us.lsi.dad;
//
//
//
//
//import java.util.ArrayList;
//import java.util.List;
//
//import com.google.gson.Gson;
//import com.google.gson.GsonBuilder;
//
//import io.vertx.core.AbstractVerticle;
//import io.vertx.core.Promise;
//import io.vertx.ext.web.Router;
//import io.vertx.ext.web.RoutingContext;
//import io.vertx.ext.web.handler.BodyHandler;
//import io.vertx.mysqlclient.MySQLConnectOptions;
//import io.vertx.mysqlclient.MySQLPool;
//import io.vertx.sqlclient.PoolOptions;
//import io.vertx.sqlclient.Row;
//import io.vertx.sqlclient.RowSet;
//import io.vertx.sqlclient.Tuple;
//
//public class RestServer extends AbstractVerticle {
//
//	MySQLPool mySqlClient;
//	private Gson gson;
//
//	public void start(Promise<Void> startFuture) {
//		
//		MySQLConnectOptions connectOptions = new MySQLConnectOptions().setPort(3306).setHost("localhost")
//				.setDatabase("dad").setUser("root").setPassword("iissi$root");
//		PoolOptions poolOptions  = new PoolOptions().setMaxSize(5);
//		mySqlClient = MySQLPool.pool(vertx, connectOptions, poolOptions);
//		
//
//		// Creating some synthetic data
//		//createSomeData(25);
//
//		// Instantiating a Gson serialize object using specific date format
//		gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
//
//		// Defining the router object
//		Router router = Router.router(vertx);
//
//		// Handling any server startup result
//		vertx.createHttpServer().requestHandler(router::handle).listen(8080, result -> {
//			if (result.succeeded()) {
//				startFuture.complete();
//			} else {
//				startFuture.fail(result.cause());
//			}
//		});
//
//		// Defining URI paths for each method in RESTful interface, including body
//		// handling by /api/users* or /api/users/*
//		router.route("/api/*").handler(BodyHandler.create());
//		
//		router.get("/api/sensor").handler(this::getAllSensor);
//		router.get("/api/sensor/:sensorid").handler(this::getBySensor);
//		router.get("/api/sensor/:sensorid").handler(this::getLastBySensor);
//		//router.post("/api/sensor").handler(this::addOneSensorValue);
//		router.post("/api/sensor").handler(this::addSensor);
//		
//		
//		router.get("/api/actuador").handler(this::getAllActuador);
//		router.get("/api/actuador/:actuadorid").handler(this::getByActuador);
//		router.get("/api/actuador/:actuadorid").handler(this::getLastByActuador);
//		//router.post("/api/actuador").handler(this::addOneActuadorValue);
//		router.post("/api/actuador").handler(this::addActuador);
//	}
//
//	private void getAllSensor(RoutingContext routingContext) {
//		mySqlClient.query("SELECT * FROM dad.sensor;").execute(res -> {
//			if (res.succeeded()) {
//				// Get the result set
//				RowSet<Row> resultSet = res.result(); 
//				System.out.println(resultSet.size()); 
//				List<SensorEntity> result = new ArrayList<>();
//				for (Row elem: resultSet) {
//					result.add(new SensorEntity(elem.getInteger ("idSensor"), 
//					elem.getLong("timestamp"), elem.getBoolean("detecta"), elem.getInteger("distancia"), elem.getInteger("idPlaca"), elem.getInteger("idGroup")));
//				}
//				System.out.println(result.toString());
//				routingContext.response()
//				.setStatusCode(201)
//				.end("Datos del sensor recibidos correctamente");
//				
//			} else {
//				System.out.println("Error: " + res.cause().getLocalizedMessage());
//				routingContext.response()
//				.setStatusCode(201)
//				.end("Datos del sensor no recibidos correctamente");
//			}
//		});
//	}
//	
//	private void getAllActuador(RoutingContext routingContext) {
//		mySqlClient.query("SELECT * FROM dad.actuador;").execute(res -> {
//			if (res.succeeded()) {
//				// Get the result set
//				RowSet<Row> resultSet = res.result(); 
//				System.out.println(resultSet.size()); 
//				List<ActuadorEntity> result = new ArrayList<>();
//				for (Row elem: resultSet) {
//					result.add(new ActuadorEntity(elem.getInteger ("idActuador"), 
//							elem.getLong("timestamp"), elem.getDouble("anguloApertura"), elem.getInteger("idPlaca"), elem.getInteger("idGroup")));
//					
//				}
//				System.out.println(result.toString());
//				routingContext.response()
//				.setStatusCode(201)
//				.end("Datos del actuador recibidos correctamente");
//				
//			} else {
//				System.out.println("Error: " + res.cause().getLocalizedMessage());
//				routingContext.response()
//				.setStatusCode(201)
//				.end("Datos del actuador no recibidos correctamente");
//			}
//		});
//	}
//
//	
//	private void getBySensor(RoutingContext routingContext) {
//		int id = Integer.parseInt(routingContext.request().getParam("sensorid"));
//		mySqlClient.getConnection(connection -> { 
//			if (connection. succeeded()) {
//				connection.result().preparedQuery("SELECT * FROM dad.sensor WHERE idSensor = ?").execute(
//						Tuple.of (id), res -> {
//							if (res.succeeded()) {
//								// Get the result set
//								RowSet<Row> resultSet = res.result(); 
//								System.out.println(resultSet.size()); 
//								List<SensorEntity> result = new ArrayList<>(); 
//								for (Row elem: resultSet) {
//									result.add(new SensorEntity(elem.getInteger ("idSensor"), 
//											elem.getLong("timestamp"), elem.getBoolean("detecta"), elem.getInteger("distancia"), elem.getInteger("idPlaca"), elem.getInteger("idGroup")));
//									
//										
//								}
//								System.out.println(result.toString());
//								routingContext.response()
//									.setStatusCode(201)
//									.end("Datos del sensor recibidos correctamente");
//								
//							}else{
//								System.out.println("Error: " + res.cause().getLocalizedMessage());
//								routingContext.response()
//								.setStatusCode(201)
//								.end("Datos del sensor no recibidos correctamente");
//							
//							}
//							connection.result().close();
//						});
//			} else {
//				System.out.println(connection.cause().toString());
//			}
//		});
//	}
//
//	
//	
//	
//	private void getLastBySensor(RoutingContext routingContext) {
//		int id = Integer.parseInt(routingContext.request().getParam("sensorid"));
//		mySqlClient.getConnection(connection -> { 
//			if (connection. succeeded()) {
//				connection.result().preparedQuery("SELECT * FROM dad.sensor WHERE idSensor = ? ORDER BY timestamp DESC LIMIT 1").execute(
//						Tuple.of (id), res -> {
//							if (res.succeeded()) {
//								// Get the result set
//								RowSet<Row> resultSet = res.result(); 
//								System.out.println(resultSet.size()); 
//								List<SensorEntity> result = new ArrayList<>(); 
//								for (Row elem: resultSet) {
//									result.add(new SensorEntity(elem.getInteger ("idSensor"), 
//											elem.getLong("timestamp"), elem.getBoolean("detecta"), elem.getInteger("distancia"), elem.getInteger("idPlaca"), elem.getInteger("idGroup")));
//									
//										
//								}
//								System.out.println(result.toString());
//								routingContext.response()
//								.setStatusCode(201)
//								.end("Datos del sensor recibidos correctamente");
//								
//							}else{
//								System.out.println("Error: " + res.cause().getLocalizedMessage());
//								routingContext.response()
//								.setStatusCode(201)
//								.end("Datos del sensor no recibidos correctamente");
//							
//							}
//							connection.result().close();
//						});
//			} else {
//				System.out.println(connection.cause().toString());
//			}
//		});
//	}
//	
//	private void getByActuador(RoutingContext routingContext) {
//		int id = Integer.parseInt(routingContext.request().getParam("actuadorid"));
//		mySqlClient.getConnection(connection -> { 
//			if (connection. succeeded()) {
//				connection.result().preparedQuery("SELECT * FROM dad.actuador WHERE idActuador = ?").execute(
//						Tuple.of (id), res -> {
//							if (res.succeeded()) {
//								// Get the result set
//								RowSet<Row> resultSet = res.result(); 
//								System.out.println(resultSet.size()); 
//								List<ActuadorEntity> result = new ArrayList<>(); 
//								for (Row elem: resultSet) {
//									result.add(new ActuadorEntity(elem.getInteger ("idActuador"), 
//											elem.getLong("timestamp"), elem.getDouble("anguloApertura"), elem.getInteger("idPlaca"), elem.getInteger("idGroup")));
//									
//										
//								}
//								System.out.println(result.toString());
//								routingContext.response()
//								.setStatusCode(201)
//								.end("Datos del actuador recibidos correctamente");
//								
//							}else{
//								System.out.println("Error: " + res.cause().getLocalizedMessage());
//								routingContext.response()
//								.setStatusCode(201)
//								.end("Datos del actuador no recibidos correctamente");
//							
//							}
//							connection.result().close();
//						});
//			} else {
//				System.out.println(connection.cause().toString());
//			}
//		});
//	}
//	
//	private void getLastByActuador(RoutingContext routingContext) {
//		int id = Integer.parseInt(routingContext.request().getParam("actuadorid"));
//		mySqlClient.getConnection(connection -> { 
//			if (connection. succeeded()) {
//				connection.result().preparedQuery("SELECT * FROM dad.actuador WHERE idActuador = ? ORDER BY timestamp DESC LIMIT 1").execute(
//						Tuple.of (id), res -> {
//							if (res.succeeded()) {
//								// Get the result set
//								RowSet<Row> resultSet = res.result(); 
//								System.out.println(resultSet.size()); 
//								List<ActuadorEntity> result = new ArrayList<>(); 
//								for (Row elem: resultSet) {
//									result.add(new ActuadorEntity(elem.getInteger ("idActuador"), 
//											elem.getLong("timestamp"), elem.getDouble("anguloApertura"), elem.getInteger("idPlaca"), elem.getInteger("idGroup")));
//									
//										
//								}
//								System.out.println(result.toString());
//								routingContext.response()
//								.setStatusCode(201)
//								.end("Datos del actuador recibidos correctamente");
//								
//							}else{
//								System.out.println("Error: " + res.cause().getLocalizedMessage());
//								routingContext.response()
//								.setStatusCode(201)
//								.end("Datos del actuador no recibidos correctamente");
//							
//							}
//							connection.result().close();
//						});
//			} else {
//				System.out.println(connection.cause().toString());
//			}
//		});
//	}
//	
//
//	private void addSensor(RoutingContext routingContext) {
//
//		// Parseamos el cuerpo de la solicitud HTTP a un objeto Sensor_humedad_Entity
//		final SensorEntity sensor = gson.fromJson(routingContext.getBodyAsString(),
//				SensorEntity.class);
//
//		// Ejecutamos la inserción en la base de datos MySQL
//		
//		mySqlClient.preparedQuery(
//						"INSERT INTO sensor (idSensor, timestamp, detecta, distancia, idPlaca, idGroup) VALUES (?, ?, ?, ?, ?, ?)")
//				.execute((Tuple.of(sensor.getIdSensor(), sensor.getTimestamp(), sensor.getDetecta(), sensor.getDistancia(),
//						sensor.getIdPlaca(), sensor.getIdGroup())), res -> {
//							if (res.succeeded()) {
//								// Si la inserción es exitosa, respondemos con el sensor creado
//								routingContext.response().setStatusCode(201).putHeader("content-type",
//										"application/json; charset=utf-8").end("Sensor añadido correctamente");
//							} else {
//								// Si hay un error en la inserción, respondemos con el mensaje de error
//								System.out.println("Error: " + res.cause().getLocalizedMessage());
//								routingContext.response().setStatusCode(500).end("Sensor al añadir el actuador: " + res.cause().getMessage());
//							}
//						});
//	}
//	
//	private void addActuador(RoutingContext routingContext) {
//
//		// Parseamos el cuerpo de la solicitud HTTP a un objeto Sensor_humedad_Entity
//		final ActuadorEntity actuador = gson.fromJson(routingContext.getBodyAsString(),
//				ActuadorEntity.class);	
//
//		// Ejecutamos la inserción en la base de datos MySQL
//		
//		mySqlClient.preparedQuery(
//						"INSERT INTO actuador (idActuador, timestamp, anguloApertura, idPlaca, idGroup) VALUES (?, ?, ?, ?, ?)")
//				.execute((Tuple.of(actuador.getIdActuador(), actuador.getTimestamp(), actuador.getAnguloApertura(),
//						actuador.getIdPlaca(), actuador.getIdGroup())), res -> {
//							if (res.succeeded()) {
//								// Si la inserción es exitosa, respondemos con el sensor creado
//								routingContext.response().setStatusCode(201).putHeader("content-type",
//										"application/json; charset=utf-8").end("Actuador añadido correctamente");
//							} else {
//								// Si hay un error en la inserción, respondemos con el mensaje de error
//								System.out.println("Error: " + res.cause().getLocalizedMessage());
//								routingContext.response().setStatusCode(500).end("Error al añadir el actuador: " + res.cause().getMessage());
//							}
//						});
//	}
//	
//}