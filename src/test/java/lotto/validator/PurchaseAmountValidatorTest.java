package lotto.validator;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

public class PurchaseAmountValidatorTest {
    PurchaseAmountValidator purchaseAmountValidator = new PurchaseAmountValidator();

    @DisplayName("구매 금액으로 Null 및 공백 입력 시 예외가 발생한다.")
    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "   ", "\t", "\n"})
    void testWhenPurchaseAmountIsNullOrBlank(String purchaseAmount) {
        IllegalArgumentException purchaseAmountError =
                Assertions.assertThrows(IllegalArgumentException.class,
                        () -> purchaseAmountValidator.validate(purchaseAmount));
        assertThat(purchaseAmountError.getMessage()).contains("[ERROR]");
    }

    @DisplayName("구매 금액으로 숫자가 아닌 값 입력 시 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(strings = {"오천원", "five", "$", "5$"})
    void testWhenPurchaseAmountIsNotNumeric(String purchaseAmount) {
        IllegalArgumentException purchaseAmountError =
                Assertions.assertThrows(IllegalArgumentException.class,
                        () -> purchaseAmountValidator.validate(purchaseAmount));
        assertThat(purchaseAmountError.getMessage()).contains("[ERROR]");
    }

    @DisplayName("구매 금액으로 1,000원으로 나누어 떨어지지 않는 값 입력 시 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(strings = {"0", "500", "1300", "39900", "123456789"})
    void testWhenPurchaseAmountCannotDivideOneThousand(String purchaseAmount) {
        IllegalArgumentException purchaseAmountError =
                Assertions.assertThrows(IllegalArgumentException.class,
                        () -> purchaseAmountValidator.validate(purchaseAmount));
        assertThat(purchaseAmountError.getMessage()).contains("[ERROR]");
    }

    @DisplayName("적절한 구매 금액 입력 시 예외가 발생하지 않는다.")
    @ParameterizedTest
    @ValueSource(strings = {"1000", "20000", "35000", "1999999000"})
    void testWhenPurchaseAmountIsValid(String purchaseAmount) {
        Assertions.assertDoesNotThrow(() -> purchaseAmountValidator.validate(purchaseAmount));
    }
}
