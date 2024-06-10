package es.us.lsi.dad;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import io.netty.handler.codec.mqtt.MqttQoS;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.mqtt.MqttClient;
import io.vertx.mqtt.MqttClientOptions;
import io.vertx.mysqlclient.MySQLConnectOptions;
import io.vertx.mysqlclient.MySQLPool;
import io.vertx.sqlclient.PoolOptions;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowSet;
import io.vertx.sqlclient.Tuple;
import io.vertx.core.buffer.Buffer;


public class RestServer extends AbstractVerticle {

	
	/************************/
	/*****INICIO REST********/
	/************************/
	
	
	private Gson gson;
	
	MySQLPool mySQLclient; 
	MqttClient mqttClient;
	
	public void start(Promise<Void> startFuture) {

		///

		
		
		// Establezco mqtt
	
		
		
		///
		
		mqttClient = MqttClient.create(vertx, new MqttClientOptions().setAutoKeepAlive(true));
		
		mqttClient.connect(1883, "localhost", s -> {

			mqttClient.subscribe("twmp", MqttQoS.AT_LEAST_ONCE.value(), handler -> {
				if (handler.succeeded()) {
					System.out.println("Suscripción " + mqttClient.clientId());
				}
			});
		
		});
		
		MySQLConnectOptions connectOptions = new MySQLConnectOptions().setPort(3306).setHost("localhost")
				.setDatabase("proyectodad").setUser("root").setPassword("root");
		

		PoolOptions poolOptions = new PoolOptions().setMaxSize(5);
		
		mySQLclient = MySQLPool.pool(vertx, connectOptions, poolOptions);
		
		gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
		
				// Defining the router object
		Router router = Router.router(vertx);
				// Handling any server startup result
		vertx.createHttpServer().requestHandler(router::handle).listen(8084, result -> {
			if (result.succeeded()) {
				startFuture.complete();
			} else {
				startFuture.fail(result.cause());
			}
		});

		router.route("/api/*").handler(BodyHandler.create());

		router.get("/api/temperaturas").handler(this::getAllST);
		router.get("/api/temperaturas/:idtemp").handler(this::getBySensorT);
		router.get("/api/temperaturas/:idtemp").handler(this::getLastBySensorT);//no
		router.post("/api/temperaturas").handler(this::addSensorT);//no
		
		router.get("/api/luces").handler(this::getAllSL);
		router.get("/api/luces/:idl").handler(this::getBySensorL);
		router.get("/api/luces/:idl").handler(this::getLastBySensorL);//no
		//router.post("/api/luces").handler(this::addSensorL);//no

		router.get("/api/actled").handler(this::getAllAL);
		router.get("/api/actled/:idla").handler(this::getByActL);
		router.get("/api/actled/:idla").handler(this::getLastByActL);
		router.post("/api/actled").handler(this::addAL);

		router.get("/api/actfan").handler(this::getAllAF);
		router.get("/api/actfan/:idfa").handler(this::getByActF);
		router.get("/api/actfan/:idfa").handler(this::getLastByActF);
		router.post("/api/actfan").handler(this::addAF);
	
	}
	
	@Override
	public void stop(Promise<Void> stopPromise) throws Exception {
		try {
			stopPromise.complete();
		} catch (Exception e) {
			stopPromise.fail(e);
		}
		super.stop(stopPromise);
	}
	
	/*************************/
	/*****STEMPERATURA********/
	/*************************/
	
	private void getAllST(RoutingContext routingContext) {
		mySQLclient.query("SELECT * FROM proyectodad.sensortemperatura;").execute(res -> {
			if (res.succeeded()) {
				// Get the result set
				RowSet<Row> resultSet = res.result(); 
				System.out.println(resultSet.size()); 
				List<SensorTemperaturaImpl> result = new ArrayList<>();
				for (Row elem: resultSet) {
					result.add(new SensorTemperaturaImpl(elem.getInteger ("idtemp"),
					elem.getDouble("temperatura"), elem.getLong("timestampt"),
					elem.getInteger("idP"), elem.getInteger("idG")));
				}
				System.out.println(result.toString());
				routingContext.response()
				.setStatusCode(201)
				.end("Datos del sensor recibidos correctamente");
				
			} else {
				System.out.println("Error: " + res.cause().getLocalizedMessage());
				routingContext.response()
				.setStatusCode(201)
				.end("Datos del sensor no recibidos correctamente");
			}
		});
	}	
		
	private void getBySensorT(RoutingContext routingContext) {
		int id = Integer.parseInt(routingContext.request().getParam("idtemp"));
		mySQLclient.getConnection(connection -> { 
			if (connection. succeeded()) {
				connection.result().preparedQuery("SELECT * FROM proyectodad.sensortemperatura WHERE idtemp = ?").execute(
						Tuple.of (id), res -> {
							if (res.succeeded()) {
								// Get the result set
								RowSet<Row> resultSet = res.result(); 
								System.out.println(resultSet.size()); 
								List<SensorTemperaturaImpl> result = new ArrayList<>(); 
								for (Row elem: resultSet) {
									result.add(new SensorTemperaturaImpl(elem.getInteger ("idtemp"), 
											elem.getDouble("temperatura"), elem.getLong("timestampt"), 
											elem.getInteger("idP"), elem.getInteger("idG")));
								}
								System.out.println(result.toString());
								routingContext.response()
									.setStatusCode(201)
									.end("Datos del sensor recibidos correctamente");
							}else{
								System.out.println("Error: " + res.cause().getLocalizedMessage());
								routingContext.response()
								.setStatusCode(201)
								.end("Datos del sensor no recibidos correctamente");
							}
							connection.result().close();
						});
			} else {
				System.out.println(connection.cause().toString());
			}
		});
	}
	
	private void getLastBySensorT(RoutingContext routingContext) {
		int id = Integer.parseInt(routingContext.request().getParam("idtemp"));
		mySQLclient.getConnection(connection -> { 
			if (connection. succeeded()) {
				connection.result().preparedQuery("SELECT * FROM proyectodad.sensortemperatura WHERE idtemp = ? ORDER BY timestampt DESC LIMIT 1").execute(
						Tuple.of (id), res -> {
							if (res.succeeded()) {
								// Get the result set
								RowSet<Row> resultSet = res.result(); 
								System.out.println(resultSet.size()); 
								List<SensorTemperaturaImpl> result = new ArrayList<>(); 
								for (Row elem: resultSet) {
									result.add(new SensorTemperaturaImpl(elem.getInteger ("idtemp"), elem.getDouble("temperatura"), 
											elem.getLong("timestampt"), elem.getInteger("idP"), elem.getInteger("idG")));									
								}
								System.out.println(result.toString());
								routingContext.response()
								.setStatusCode(201)
								.end("Datos del sensor recibidos correctamente");
							}else{
								System.out.println("Error: " + res.cause().getLocalizedMessage());
								routingContext.response()
								.setStatusCode(201)
								.end("Datos del sensor no recibidos correctamente");
							
							}
							connection.result().close();
						});
			} else {
				System.out.println(connection.cause().toString());
			}
		});
	}
	
	private void addSensorT(RoutingContext routingContext) {
		final SensorTemperaturaImpl temp = gson.fromJson(routingContext.getBodyAsString(),SensorTemperaturaImpl.class);
		temp.setTimestampt(Calendar.getInstance().getTimeInMillis());
		mySQLclient.preparedQuery(
						"INSERT INTO sensortemperatura (idtemp, temperatura, timestampt, idP, idG) VALUES (?, ?, ?, ?, ?)")
				.execute((Tuple.of(temp.getIdtemp(), temp.getTemperatura(), temp.getTimestampt(), 
						temp.getIdP(), temp.getIdG())), res -> {
							if (res.succeeded()) {
								if (mqttClient != null) {
									if(temp.temperatura > 30.0) {
										mqttClient.publish("twmp", Buffer.buffer("ON"), MqttQoS.AT_LEAST_ONCE, false, false);
									}else{
										mqttClient.publish("twmp", Buffer.buffer("OFF"), MqttQoS.AT_LEAST_ONCE, false, false);
									}
							}else {
							    System.out.println("mqttClient is null. Cannot publish message.");

							}
								routingContext.response().setStatusCode(201)
									.putHeader("content-type","application/json; charset=utf-8")
									.end("Sensor añadido correctamente");
							} else {
								System.out.println("Error: " + res.cause().getLocalizedMessage());
								routingContext.response().setStatusCode(500).end("Error al añadir el sensor " + res.cause().getMessage());
							}
						});
		
		
		
	}


	
	
	/****************/
	/*****LUZ QLS****/
	/****************/
	private void getAllSL(RoutingContext routingContext) {
		mySQLclient.query("SELECT * FROM proyectodad.sensorluz;").execute(res -> {
			if (res.succeeded()) {
				// Get the result set
				RowSet<Row> resultSet = res.result(); 
				System.out.println(resultSet.size()); 
				List<SensorLuzImpl> result = new ArrayList<>();
				for (Row elem: resultSet) {
					result.add(new SensorLuzImpl(elem.getInteger ("idl"),
					elem.getDouble("nivel_luz"), elem.getLong("timestampl"),
					elem.getInteger("idP"), elem.getInteger("idG")));
				}
				System.out.println(result.toString());
				routingContext.response()
				.setStatusCode(201)
				.end("Datos del sensor recibidos correctamente");
				
			} else {
				System.out.println("Error: " + res.cause().getLocalizedMessage());
				routingContext.response()
				.setStatusCode(201)
				.end("Datos del sensor no recibidos correctamente");
			}
		});
	}	
	private void getBySensorL(RoutingContext routingContext) {
		int id = Integer.parseInt(routingContext.request().getParam("idl"));
		mySQLclient.getConnection(connection -> { 
			if (connection. succeeded()) {
				connection.result().preparedQuery("SELECT * FROM proyectodad.sensorluz WHERE idl = ?").execute(
						Tuple.of (id), res -> {
							if (res.succeeded()) {
								// Get the result set
								RowSet<Row> resultSet = res.result(); 
								System.out.println(resultSet.size()); 
								List<SensorLuzImpl> result = new ArrayList<>(); 
								for (Row elem: resultSet) {
									result.add(new SensorLuzImpl(elem.getInteger ("idl"), 
											elem.getDouble("nivel_luz"), elem.getLong("timestampl"),
											elem.getInteger("idP"), elem.getInteger("idG")));
								}
								System.out.println(result.toString());
								routingContext.response()
									.setStatusCode(201)
									.end("Datos del sensor recibidos correctamente");
							}else{
								System.out.println("Error: " + res.cause().getLocalizedMessage());
								routingContext.response()
								.setStatusCode(201)
								.end("Datos del sensor no recibidos correctamente");
							}
							connection.result().close();
						});
			} else {
				System.out.println(connection.cause().toString());
			}
		});
	}

	private void getLastBySensorL(RoutingContext routingContext) {
		int id = Integer.parseInt(routingContext.request().getParam("idl"));
		mySQLclient.getConnection(connection -> { 
			if (connection. succeeded()) {
				connection.result().preparedQuery("SELECT * FROM proyectodad.sensorluz WHERE idl = ? ORDER BY timestampl DESC LIMIT 1").execute(
						Tuple.of (id), res -> {
							if (res.succeeded()) {
								// Get the result set
								RowSet<Row> resultSet = res.result(); 
								System.out.println(resultSet.size()); 
								List<SensorLuzImpl> result = new ArrayList<>(); 
								for (Row elem: resultSet) {
									result.add(new SensorLuzImpl(elem.getInteger ("idl"), elem.getDouble("nivel_luz"), 
											elem.getLong("timestampl"), elem.getInteger("idP"), elem.getInteger("idG")));									
								}
								System.out.println(result.toString());
								routingContext.response()
								.setStatusCode(201)
								.end("Datos del sensor recibidos correctamente");
							}else{
								System.out.println("Error: " + res.cause().getLocalizedMessage());
								routingContext.response()
								.setStatusCode(201)
								.end("Datos del sensor no recibidos correctamente");
							
							}
							connection.result().close();
						});
			} else {
				System.out.println(connection.cause().toString());
			}
		});
	}	
	
	/*****************/
	/*****ALED********/
	/*****************/
	
	private void getAllAL(RoutingContext routingContext) {
		mySQLclient.query("SELECT * FROM proyectodad.actuadorled;").execute(res -> {
			if (res.succeeded()) {
				// Get the result set
				RowSet<Row> resultSet = res.result(); 
				System.out.println(resultSet.size()); 
				List<ActLedImpl> result = new ArrayList<>();
				for (Row elem: resultSet) {
					result.add(new ActLedImpl(elem.getInteger ("idla"),elem.getDouble("nivel_luz"), 
							elem.getLong("timestample"), elem.getInteger("idP"), elem.getInteger("idG")));
				}
				System.out.println(result.toString());
				routingContext.response()
				.setStatusCode(201)
				.end("Datos del actuador recibidos correctamente");
				
			} else {
				System.out.println("Error: " + res.cause().getLocalizedMessage());
				routingContext.response()
				.setStatusCode(201)
				.end("Datos del actuador no recibidos correctamente");
			}
		});
	}
	
	private void getByActL(RoutingContext routingContext) {
		int id = Integer.parseInt(routingContext.request().getParam("idla"));
		mySQLclient.getConnection(connection -> { 
			if (connection. succeeded()) {
				connection.result().preparedQuery("SELECT * FROM proyectodad.actuadorled WHERE idla = ?").execute(
						Tuple.of (id), res -> {
							if (res.succeeded()) {
								// Get the result set
								RowSet<Row> resultSet = res.result(); 
								System.out.println(resultSet.size()); 
								List<ActLedImpl> result = new ArrayList<>(); 
								for (Row elem: resultSet) {
									result.add(new ActLedImpl(elem.getInteger ("idla"), elem.getDouble("nivel_luz"),  
											elem.getLong("timestample"), elem.getInteger("idP"), 
											elem.getInteger("idG")));
								}
								System.out.println(result.toString());
								routingContext.response()
									.setStatusCode(201)
									.end("Datos del actuador recibidos correctamente");
							}else{
								System.out.println("Error: " + res.cause().getLocalizedMessage());
								routingContext.response()
								.setStatusCode(201)
								.end("Datos del actuador no recibidos correctamente");
							}
							connection.result().close();
						});
			} else {
				System.out.println(connection.cause().toString());
			}
		});
	}
	private void getLastByActL(RoutingContext routingContext) {
		int id = Integer.parseInt(routingContext.request().getParam("idla"));
		mySQLclient.getConnection(connection -> { 
			if (connection. succeeded()) {
				connection.result().preparedQuery("SELECT * FROM proyectodad.actuadorled WHERE idla = ? ORDER BY timestample DESC LIMIT 1").execute(
						Tuple.of (id), res -> {
							if (res.succeeded()) {
								// Get the result set
								RowSet<Row> resultSet = res.result(); 
								System.out.println(resultSet.size()); 
								List<ActLedImpl> result = new ArrayList<>(); 
								for (Row elem: resultSet) {
									result.add(new ActLedImpl(elem.getInteger ("idla"), elem.getDouble("nivel_luz"), 
											elem.getLong("timestample"), elem.getInteger("idP"), elem.getInteger("idG")));		
								}
								System.out.println(result.toString());
								routingContext.response()
								.setStatusCode(201)
								.end("Datos del actuador recibidos correctamente");
								
							}else{
								System.out.println("Error: " + res.cause().getLocalizedMessage());
								routingContext.response()
								.setStatusCode(201)
								.end("Datos del actuador no recibidos correctamente");
							
							}
							connection.result().close();
						});
			} else {
				System.out.println(connection.cause().toString());
			}
		});
	}
	
	private void addAL(RoutingContext routing) {
		final ActLedImpl led = gson.fromJson(routing.getBodyAsString(), ActLedImpl.class);
		
		mySQLclient.preparedQuery(
				"INSERT INTO actuadorled (idla, nivel_luz, idP, idG) VALUES (?, ?, ?, ?)").execute((Tuple.of(led.getIdLA(), led.getNivel_luz(), 
						led.getIdP(), led.getIdG())), res -> {
							if(res.succeeded()) {
								routing.response().setStatusCode(201).putHeader("content-type",
										"application/json; charset=utf-8").end("Sensor añadido");
							}else {
							System.out.println("Error: " + res.cause().getLocalizedMessage());
							routing.response()
							.setStatusCode(201)
							.end("Datos del led no recibidos");
							}
		});
	}
	
	/*****************/
	/*****AFAN********/
	/*****************/
	private void getAllAF(RoutingContext routingContext) {
		mySQLclient.query("SELECT * FROM proyectodad.actuadorfan;").execute(res -> {
			if (res.succeeded()) {
				// Get the result set
				RowSet<Row> resultSet = res.result(); 
				System.out.println(resultSet.size()); 
				List<ActFanImpl> result = new ArrayList<>();
				for (Row elem: resultSet) {
					result.add(new ActFanImpl(elem.getInteger ("idfa"),elem.getInteger("onoff"), elem.getLong("timestampf"), 
							elem.getInteger("idP"), elem.getInteger("idG")));
			}
				System.out.println(result.toString());
				routingContext.response()
				.setStatusCode(201)
				.end("Datos del actuador recibidos correctamente");
				
			} else {
				System.out.println("Error: " + res.cause().getLocalizedMessage());
				routingContext.response()
				.setStatusCode(201)
				.end("Datos del actuador no recibidos correctamente");
			}
		});
	}

	private void getByActF(RoutingContext routingContext) {
		int id = Integer.parseInt(routingContext.request().getParam("idfa"));
		mySQLclient.getConnection(connection -> { 
			if (connection. succeeded()) {
				connection.result().preparedQuery("SELECT * FROM proyectodad.actuadorfan WHERE idfa = ?").execute(
						Tuple.of (id), res -> {
							if (res.succeeded()) {
								// Get the result set
								RowSet<Row> resultSet = res.result(); 
								System.out.println(resultSet.size()); 
								List<ActFanImpl> result = new ArrayList<>(); 
								for (Row elem: resultSet) {
									result.add(new ActFanImpl(elem.getInteger ("idfa"), elem.getInteger("onoff"), 
											elem.getLong("timestampf"), elem.getInteger("idP"), elem.getInteger("idG")));
								}
								System.out.println(result.toString());
								routingContext.response()
									.setStatusCode(201)
									.end("Datos del actuador recibidos correctamente");
							}else{
								System.out.println("Error: " + res.cause().getLocalizedMessage());
								routingContext.response()
								.setStatusCode(201)
								.end("Datos del actuador no recibidos correctamente");
							}
							connection.result().close();
						});
			} else {
				System.out.println(connection.cause().toString());
			}
		});
	}
	
	private void getLastByActF(RoutingContext routingContext) {
		int id = Integer.parseInt(routingContext.request().getParam("idfa"));
		mySQLclient.getConnection(connection -> { 
			if (connection. succeeded()) {
				connection.result().preparedQuery("SELECT * FROM proyectodad.actuadorfan WHERE idla = ? ORDER BY timestampf DESC LIMIT 1").execute(
						Tuple.of (id), res -> {
							if (res.succeeded()) {
								// Get the result set
								RowSet<Row> resultSet = res.result(); 
								System.out.println(resultSet.size()); 
								List<ActFanImpl> result = new ArrayList<>(); 
								for (Row elem: resultSet) {
									result.add(new ActFanImpl(elem.getInteger ("idfa"), elem.getInteger("onoff"), 
											elem.getLong("timestampf"), elem.getInteger("idP"), elem.getInteger("idG")));		
								}
								System.out.println(result.toString());
								routingContext.response()
								.setStatusCode(201)
								.end("Datos del actuador recibidos correctamente");
								
							}else{
								System.out.println("Error: " + res.cause().getLocalizedMessage());
								routingContext.response()
								.setStatusCode(201)
								.end("Datos del actuador no recibidos correctamente");
							
							}
							connection.result().close();
						});
			} else {
				System.out.println(connection.cause().toString());
			}
		});
	}
	
	private void addAF(RoutingContext routing) {
		final ActFanImpl led = gson.fromJson(routing.getBodyAsString(), ActFanImpl.class);
		
		mySQLclient.preparedQuery(
				"INSERT INTO actuadorfan (idla, nivel_luz, timestampf,  idP, idG) VALUES (?, ?, ?, ?, ?)").execute((Tuple.of(led.getIdfa(), 
						led.getOnoff(),	led.getTimestampf(), led.getIdP(), led.getIdG())), res -> {
							if(res.succeeded()) {
								routing.response().setStatusCode(201).putHeader("content-type",
										"application/json; charset=utf-8").end("Sensor añadido");
							}else {
							System.out.println("Error: " + res.cause().getLocalizedMessage());
							routing.response()
							.setStatusCode(201)
							.end("Datos del act no recibidos");
							}
		});
	}
	
	/************************/
	/*****FINALIZO REST******/
	/************************/
	
}