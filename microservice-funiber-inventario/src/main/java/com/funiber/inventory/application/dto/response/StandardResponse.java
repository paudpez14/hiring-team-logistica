package com.funiber.inventory.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StandardResponse<T> {
	private Integer httpStatus;
    private String status;
	private String message;
	private String internalCode;
    private T data;

	/* Creates an API Response for a successful operation
	 * @param data The data to include in the response
	 * @param responseHashMap A map containing response messages
	 * @param key The key corresponding to the desired response messsage
	 * @param <T> The type of data to be included in the response
	 * @return A StandardResponse indicating a sucessful operation
	*/
	public static <T> StandardResponse<T> ok (T data, String response, String key){
		return StandardResponse.<T>builder()
					.httpStatus(HttpStatus.OK.value())
					.status(ConstantResponse.RESULT_OK.desc)
					.internalCode(key)
					.data(data)
					.message(response)
					.build();
	}

	/* Creates an API Response for a bad request operation
	 * @param data The data to include in the response
	 * @param responseHashMap A map containing response messages
	 * @param key The key corresponding to the desired response messsage
	 * @param <T> The type of data to be included in the response
	 * @return A StandardResponse indicating a failed operation
	*/
	public static <T> StandardResponse<T> badRequest (T data, String response, String key){
		return StandardResponse.<T>builder()
					.httpStatus(HttpStatus.BAD_REQUEST.value())
					.status(ConstantResponse.RESULT_KO.desc)
					.internalCode(key)
					.data(data)
					.message(response)
					.build();
	}

	/* Creates an API Response for a not found operation
	 * @param data The data to include in the response
	 * @param responseHashMap A map containing response messages
	 * @param key The key corresponding to the desired response messsage
	 * @param <T> The type of data to be included in the response
	 * @return A StandardResponse indicating a failed operation
	*/
	public static <T> StandardResponse<T> notFound (T data, String response, String key){
		return StandardResponse.<T>builder()
					.httpStatus(HttpStatus.NOT_FOUND.value())
					.status(ConstantResponse.RESULT_KO.desc)
					.internalCode(key)
					.data(data)
					.message(response)
					.build();
	}

	/* Creates an API Response for a internal server error
	 * @param data The data to include in the response
	 * @param responseHashMap A map containing response messages
	 * @param key The key corresponding to the desired response messsage
	 * @param <T> The type of data to be included in the response
	 * @return A StandardResponse indicating a failed operation
	*/
	public static <T> StandardResponse<T> internalServer (T data, String response, String key){
		return StandardResponse.<T>builder()
					.httpStatus(HttpStatus.INTERNAL_SERVER_ERROR.value())
					.status(ConstantResponse.RESULT_KO.desc)
					.internalCode(key)
					.data(data)
					.message(response)
					.build();
	}

}
