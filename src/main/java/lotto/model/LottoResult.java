package lotto.model;

import static lotto.validator.constants.ExceptionMessage.*;
import static lotto.validator.constants.Pattern.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import lotto.model.constans.WinningPrize;
import lotto.validator.BonusNumberValidator;
import lotto.validator.WinningNumbersValidator;

public class LottoResult {
    private final List<Integer> winningNumbers;
    private int bonusNumber;
    private static final WinningNumbersValidator WINNING_NUMBERS_VALIDATOR = new WinningNumbersValidator();
    private static final BonusNumberValidator BONUS_NUMBER_VALIDATOR = new BonusNumberValidator();

    private LottoResult(String winningNumbers) {
        this.winningNumbers = splitWinningNumbers(winningNumbers);
    }

    private List<Integer> splitWinningNumbers(String winningNumbers) {
        return Arrays.stream(winningNumbers.split(NUMBER_SPLITOR.pattern()))
                .map(Integer::parseInt)
                .collect(Collectors.toList());
    }

    public static LottoResult from(String winningNumbers) {
        WINNING_NUMBERS_VALIDATOR.validate(winningNumbers);
        return new LottoResult(winningNumbers);
    }

    public void createBonusNumber(String bonusNumber) {
        BONUS_NUMBER_VALIDATOR.validate(bonusNumber);
        checkBonusNumberDuplicateWinningNumbers(bonusNumber);
        setBonusNumber(bonusNumber);
    }

    private void setBonusNumber(String bonusNumber) {
        this.bonusNumber = Integer.parseInt(bonusNumber);
    }

    public void checkBonusNumberDuplicateWinningNumbers(String bonusNumber) {
        if (winningNumbers.contains(Integer.parseInt(bonusNumber))) {
            throw new IllegalArgumentException(BONUS_NUMBER_DUPLICATES_WINNING_NUMBER_ERROR.message());
        }
    }

    public List<Integer> calculateLottosResult(List<Lotto> lottos) {
        List<Integer> lottoResults = new ArrayList<>(Collections.nCopies(WinningPrize.values().length, 0));
        for (Lotto lotto : lottos) {
            int prizeRank = calculatePrizeRank(lotto);
            int currentValue = lottoResults.get(prizeRank);
            lottoResults.set(prizeRank, currentValue + 1);
        }
        return lottoResults;
    }

    private int calculatePrizeRank(Lotto lotto) {
        int matchingNumberCount = countMatchingNumbers(lotto);
        boolean matchBonusNumber = matchBonusNumber(lotto);
        return WinningPrize.getRankByResult(matchingNumberCount, matchBonusNumber);
    }

    private int countMatchingNumbers(Lotto lotto) {
        int matchingNumberCount = 0;
        for (int winningNumber : winningNumbers) {
            if (lotto.contains(winningNumber)) {
                matchingNumberCount++;
            }
        }
        return matchingNumberCount;
    }

    private boolean matchBonusNumber(Lotto lotto) {
        return lotto.contains(bonusNumber);
    }
}
