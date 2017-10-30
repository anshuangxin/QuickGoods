package gly.kuaihuo_yunshouyin;

import com.zmsoft.TestTool.utils.Logger;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
//@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() throws Exception {
        List<String> data = new ArrayList<>();
        data.add("songs/0.mp3");
        data.add("songs/1.mp3");
        data.add("songs/2.mp3");
        data.add("songs/3.mp3");
        data.add("songs/4.mp3");
        data.add("songs/5.mp3");
        data.add("songs/6.mp3");
        data.add("songs/7.mp3");
        data.add("songs/8.mp3");
        data.add("songs/9.mp3");
        data.add("songs/10.mp3");
        data.add("songs/点.mp3");
        data.add("songs/叮咚.mp3");
        data.add("songs/核验成功.mp3");
        data.add("songs/核验失败.mp3");
        data.add("songs/欢迎使用快货新零售云收银系统.mp3");
        data.add("songs/您有百度外卖订单.mp3");
        data.add("songs/您有饿了么外卖订单.mp3");
        data.add("songs/您有美团外卖订单.mp3");
        data.add("songs/您有堂食订单，收款已成功，到账.mp3");
        data.add("songs/您有新的订单，请及时处理.mp3");
        data.add("songs/您有新订单，已支付成功.mp3");
        data.add("songs/您有自提订单，收款已成功，到账.mp3");
        data.add("songs/您有自营外卖订单，收款已成功，到账.mp3");
        data.add("songs/收款已成功，到账.mp3");
        data.add("songs/堂食订单.mp3");
        data.add("songs/外卖订单.mp3");
        data.add("songs/元.mp3");
        data.add("songs/自提订单.mp3");
        data.add("songs/百.mp3");
        data.add("songs/万.mp3");
        data.add("songs/亿.mp3");
        data.add("songs/千.mp3");
        data.add("songs/微信收款成功.mp3");
        data.add("songs/挂单成功.mp3");
        data.add("songs/支付宝收款成功.mp3");
        data.add("songs/退单成功.mp3");
        for (int i = 0; i < data.size(); i++) {
            Logger.log(data.get(i).replace("songs/", "") + "--" + i);
        }
    }
}
