package lotto.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class LottoResultTest {
    @DisplayName("당첨 번호와 중복된 보너스 번호 입력 시 예외가 발생한다.")
    @Test
    void testWhenBonusNumberDuplicateWinningNumbers() {
        LottoResult lottoResult = LottoResult.from("1,2,3,4,5,6");

        IllegalArgumentException bonusNumberException =
                Assertions.assertThrows(IllegalArgumentException.class,
                        () -> lottoResult.createBonusNumber("6"));
        assertThat(bonusNumberException.getMessage()).contains("[ERROR]");
    }

    @DisplayName("적절한 보너스 번호 입력 시 예외가 발생하지 않는다.")
    @Test
    void testWhenBonusNumberIsValid() {
        LottoResult lottoResult = LottoResult.from("1,2,3,4,5,6");

        Assertions.assertDoesNotThrow(() -> lottoResult.createBonusNumber("20"));
    }

    @DisplayName("로또가 주어지면 일치한 당첨 번호 갯수에 맞춰서 정해진 등수의 결과를 나타낸다.")
    @Test
    void testShowLottoResults() {
        Client client = Client.from("5000");
        client.receiveLotto(new Lotto(List.of(1, 2, 3, 4, 5, 6)));
        client.receiveLotto(new Lotto(List.of(1, 2, 3, 10, 15, 20)));
        client.receiveLotto(new Lotto(List.of(1, 2, 3, 4, 10, 35)));
        client.receiveLotto(new Lotto(List.of(1, 2, 3, 4, 5, 16)));
        client.receiveLotto(new Lotto(List.of(11, 12, 13, 14, 15, 16)));
        LottoResult lottoResult = LottoResult.from("1,2,3,4,5,6");
        lottoResult.createBonusNumber("16");

        List<Integer> result = lottoResult.calculateLottosResult(client.getLottos());

        assertThat(result).isEqualTo(List.of(1, 1, 1, 0, 1, 1));
    }
}
