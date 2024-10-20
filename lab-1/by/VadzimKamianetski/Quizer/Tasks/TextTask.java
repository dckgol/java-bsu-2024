package by.VadzimKamianetski.Quizer.Tasks;

import by.VadzimKamianetski.Quizer.Result;
import by.VadzimKamianetski.Quizer.TaskGenerators.PoolTaskGenerator;

/**
 * Задание с заранее заготовленным текстом. 
 * Можно использовать {@link PoolTaskGenerator}, чтобы задавать задания такого типа.
 */
public class TextTask implements Task {
    private String text;
    private String answer;
    /**
     * @param text   текст задания
     * @param answer ответ на задание
     */
    public TextTask(
        String text,
        String answer
    ) {
        this.text = text;
        this.answer = answer;
    }
    @Override
    public String getText() {
        return text;
    }
    @Override
    public Result validate(String answer) {
        return answer.equals(this.answer) ? Result.OK : Result.WRONG;
    }
}