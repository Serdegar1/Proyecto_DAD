package es.us.lsi.dad;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;

public class RestServer extends AbstractVerticle {

	
	/************************/
	/*****INICIO REST********/
	/************************/
	
	private Map<Integer, SensorTemperaturaEntity> temperaturas = new HashMap<Integer, SensorTemperaturaEntity>();
	private Map<Integer, SensorLuzEntity> luces= new HashMap<Integer, SensorLuzEntity>();
	private Gson gson;
	
	public void start(Promise<Void> startFuture) {
		// Instantiating a Gson serialize object using specific date format
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

		// Defining URI paths for each method in RESTful interface, including body
		// handling by /api/users* or /api/users/*
		router.route("/api/temperaturas*").handler(BodyHandler.create());
		router.get("/api/temperaturas").handler(this::getAllWithParamsT);
		router.get("/api/temperaturas/temperatura/allt").handler(this::getAllT);
		router.get("/api/temperaturas/:idtempt").handler(this::getOneT);
		router.post("/api/temperaturas").handler(this::addOneT);
		router.delete("/api/temperaturas/:idtempt").handler(this::deleteOneT);
		router.put("/api/temperaturas/:idtempt").handler(this::putOneT);
		//TODO cambiar user id por luz/temperatura id, pero no se q id poner pq userid no esta en USerEntity
		router.route("/api/luces*").handler(BodyHandler.create());
		router.get("/api/luces").handler(this::getAllWithParamsL);
		router.get("/api/luces/luz/alll").handler(this::getAllL);
		router.get("/api/luces/:idluz").handler(this::getOneL);
		router.delete("/api/luces/:idluz").handler(this::deleteOneL);
		router.put("/api/luces/:idluz").handler(this::putOneL);
	}
	
	@Override
	public void stop(Promise<Void> stopPromise) throws Exception {
		try {
			temperaturas.clear();
			luces.clear();
			stopPromise.complete();
		} catch (Exception e) {
			stopPromise.fail(e);
		}
		super.stop(stopPromise);
	}
	
	/************************/
	/*****TEMPERATURA********/
	/************************/
	
	@SuppressWarnings("unused")
	private void getAllT(RoutingContext routingContext) {
		routingContext.response().putHeader("content-type", "application/json; charset=utf-8").setStatusCode(200)
				.end(gson.toJson(new TemperaturaEntityListWrapper(temperaturas.values())));
	}
	
	private void getAllWithParamsT(RoutingContext routingContext) {
		
		//TODO Comprobar qeu temperatura y timestamp son String y no Intefer y Long
		final String temperatura = routingContext.queryParams().contains("temperatura") ? 
				routingContext.queryParam("temperatura").get(0) : null;
		
		final String timestampt = routingContext.queryParams().contains("timestampt") 
				? routingContext.queryParam("timestampt").get(0) : null;
		
		Integer temperaturaint = Integer.parseInt(temperatura);
		Long timestamptlong = Long.parseLong(timestampt) ;
		
		routingContext.response().putHeader("content-type", "application/json; charset=utf-8").setStatusCode(200)
				.end(gson.toJson(new TemperaturaEntityListWrapper(temperaturas.values().stream().filter(elem -> {
					boolean res = true;
					res = res && (temperaturaint != null ? elem.getTemperatura().equals(temperaturaint) : true);
					res = res && (timestamptlong != null ? elem.getTimestampt().equals(timestamptlong) : true);
					return res;
				}).collect(Collectors.toList()))));
	}
	
	private void getOneT(RoutingContext routingContext) {
		int id = 0;
		try {
			id = Integer.parseInt(routingContext.request().getParam("idtempt"));

			if (temperaturas.containsKey(id)) {
				SensorTemperaturaEntity ds = temperaturas.get(id);
				routingContext.response().putHeader("content-type", "application/json; charset=utf-8")
						.setStatusCode(200).end(ds != null ? gson.toJson(ds) : "");
			} else {
				routingContext.response().putHeader("content-type", "application/json; charset=utf-8")
						.setStatusCode(204).end();
			}
		} catch (Exception e) {
			routingContext.response().putHeader("content-type", "application/json; charset=utf-8").setStatusCode(204)
					.end();
		}
	}
	
	private void addOneT(RoutingContext routingContext) {
		final SensorTemperaturaEntity temp = gson.fromJson(routingContext.getBodyAsString(), SensorTemperaturaEntity.class);
		temperaturas.put(temp.getIdth(), temp);
		routingContext.response().setStatusCode(201).putHeader("content-type", "application/json; charset=utf-8")
				.end(gson.toJson(temp));
	}
	
	private void deleteOneT(RoutingContext routingContext) {
		int id = Integer.parseInt(routingContext.request().getParam("idtempt"));
		if (temperaturas.containsKey(id)) {
			SensorTemperaturaEntity temp = temperaturas.get(id);
			temperaturas.remove(id);
			routingContext.response().setStatusCode(200).putHeader("content-type", "application/json; charset=utf-8")
					.end(gson.toJson(temperaturas));
		} else {
			routingContext.response().setStatusCode(204).putHeader("content-type", "application/json; charset=utf-8")
					.end();
		}
	}
	private void putOneT(RoutingContext routingContext) {
		int id = Integer.parseInt(routingContext.request().getParam("idtempt"));
		SensorTemperaturaEntity ds = temperaturas.get(id);
		final SensorTemperaturaEntity element = gson.fromJson(routingContext.getBodyAsString(), SensorTemperaturaEntity.class);
		ds.setTemperatura(element.getTemperatura());
		ds.setTimestampt(element.getTimestampt());
		temperaturas.put(ds.getIdth(), ds);
		routingContext.response().setStatusCode(201).putHeader("content-type", "application/json; charset=utf-8")
				.end(gson.toJson(element));
	}
	
	/****************/
	/*****LUZ********/
	/****************/
	
	@SuppressWarnings("unused")
	private void getAllL(RoutingContext routingContext) {
		routingContext.response().putHeader("content-type", "application/json; charset=utf-8").setStatusCode(200)
				.end(gson.toJson(new LuzEntityListWrapper(luces.values())));
	}
	private void getAllWithParamsL(RoutingContext routingContext) {
		final String luz = routingContext.queryParams().contains("nivel_luz") ? 
				routingContext.queryParam("nivel_luz").get(0) : null;
		
		final String timestampl = routingContext.queryParams().contains("timestampl") 
				? routingContext.queryParam("timestampl").get(0) : null;
		
		Integer luzint = Integer.parseInt(luz);
		Long timestamptllong = Long.parseLong(timestampl) ;
		

		routingContext.response().putHeader("content-type", "application/json; charset=utf-8").setStatusCode(200)
				.end(gson.toJson(new LuzEntityListWrapper(luces.values().stream().filter(elem -> {
					boolean res = true;
					res = res && (luzint != null ? elem.getNivel_luz().equals(luzint) : true);
					res = res && (timestampl != null ? elem.getNivel_luz().equals(timestampl) : true);
					return res;
				}).collect(Collectors.toList()))));
	}

	
	private void getOneL(RoutingContext routingContext) {
		int id = 0;
		try {
			id = Integer.parseInt(routingContext.request().getParam("idluz"));

			if (luces.containsKey(id)) {
				SensorLuzEntity ds = luces.get(id);
				routingContext.response().putHeader("content-type", "application/json; charset=utf-8")
						.setStatusCode(200).end(ds != null ? gson.toJson(ds) : "");
			} else {
				routingContext.response().putHeader("content-type", "application/json; charset=utf-8")
						.setStatusCode(204).end();
			}
		} catch (Exception e) {
			routingContext.response().putHeader("content-type", "application/json; charset=utf-8").setStatusCode(204)
					.end();
		}
	}
	
	private void addOneL(RoutingContext routingContext) {
		final SensorLuzEntity luz = gson.fromJson(routingContext.getBodyAsString(), SensorLuzEntity.class);
		luces.put(luz.getIdl(), luz);
		routingContext.response().setStatusCode(201).putHeader("content-type", "application/json; charset=utf-8")
				.end(gson.toJson(luz));
	}
	
	
	private void deleteOneL(RoutingContext routingContext) {
		int id = Integer.parseInt(routingContext.request().getParam("idluz"));
		if (luces.containsKey(id)) {
			SensorLuzEntity user = luces.get(id);
			luces.remove(id);
			routingContext.response().setStatusCode(200).putHeader("content-type", "application/json; charset=utf-8")
					.end(gson.toJson(user));
		} else {
			routingContext.response().setStatusCode(204).putHeader("content-type", "application/json; charset=utf-8")
					.end();
		}
	}
	
	private void putOneL(RoutingContext routingContext) {
		int id = Integer.parseInt(routingContext.request().getParam("userid"));
		SensorLuzEntity ds = luces.get(id);
		final SensorLuzEntity element = gson.fromJson(routingContext.getBodyAsString(), SensorLuzEntity.class);
		ds.setNivel_luz(element.getNivel_luz());
		ds.setTimestampl(element.getTimestampl());
		luces.put(ds.getIdl(), ds);
		routingContext.response().setStatusCode(201).putHeader("content-type", "application/json; charset=utf-8")
				.end(gson.toJson(element));
	}
	
	/************************/
	/*****FINALIZO REST******/
	/************************/
	
}
