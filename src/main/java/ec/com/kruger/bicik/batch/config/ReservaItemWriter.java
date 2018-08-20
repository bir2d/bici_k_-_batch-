package ec.com.kruger.bicik.batch.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import ec.com.kruger.bicik.batch.model.Reserva;


public class ReservaItemWriter implements ItemWriter<Reserva> {
	@Value("${url.enviar.notificaciones}")
	private String uri ="http://localhost:6060/rest/reserva/pruebaBach";

	@Override
	public void write(List<? extends Reserva> reservas) throws Exception {
		
		

		for (Reserva reserva : reservas) {
			//System.out.println(uri);
			System.out.println(reserva);
		}
		
		RestTemplate restTemplate = new RestTemplate();
	

		final HttpHeaders headers = new HttpHeaders();

		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

		headers.setContentType((MediaType.APPLICATION_JSON));

		if (reservas != null && !reservas.isEmpty()) {

			for (Reserva reserva : reservas) {
				try {
					@SuppressWarnings({ "rawtypes", "unchecked" })
					HttpEntity entity = new HttpEntity(reserva, headers);
					@SuppressWarnings("rawtypes")
					ResponseEntity response = restTemplate.exchange(uri, HttpMethod.POST, entity, String.class);
					if (response.getStatusCode() == HttpStatus.OK) {
						System.out.println("\n Post Success :");
					} else {
						System.out.println("\n Post Failed ");
					}

				} catch (HttpClientErrorException ex) {
					System.out.println("ERROR in ItemWriter : " + ex);

				} catch (IllegalArgumentException illegalEx) {

					System.out.println("ERROR in ItemWriter : " + illegalEx);

				}

			}

		}
		

	}

}
