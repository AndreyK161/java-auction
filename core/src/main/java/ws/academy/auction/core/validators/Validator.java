package ws.academy.auction.core.validators;

import org.springframework.stereotype.Component;
import ws.academy.auction.core.exception.StorageFileUploadException;

/**
 * Универсальный интерфейс для валидации объектов.
 *
 * @param <T> тип объекта, подлежащего валидации.
 */
@Component
public interface Validator<T> {

    /**
     * Выполняет валидацию переданного объекта.
     * <p>
     * Если объект не проходит валидацию, выбрасывается исключение.
     * Иначе метод завершается без ошибок.
     *
     * @param input объект для валидации
     * @throws StorageFileUploadException если объект не проходит проверку
     */
    void validate(T input);
}
