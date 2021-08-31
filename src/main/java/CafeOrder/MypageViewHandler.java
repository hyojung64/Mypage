package CafeOrder;

import CafeOrder.config.kafka.KafkaProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class MypageViewHandler {


    @Autowired
    private MypageRepository mypageRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void whenPayed_then_CREATE_1 (@Payload Payed payed) {
        try {
            if (payed.isMe()) {
            	// view 객체 생성
            	Mypage mypage = new Mypage();
                // view 객체에 이벤트의 Value 를 set 함
            	mypage.setOrderId(payed.getOrderId());
            	mypage.setUserId(payed.getUserId());
            	mypage.setMenuId(payed.getMenuId());
            	mypage.setPayId(payed.getId());
            	mypage.setQty(payed.getQty());
            	mypage.setStatus("Payed");
                // view 레파지 토리에 save
            	mypageRepository.save(mypage);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    @StreamListener(KafkaProcessor.INPUT)
    public void whenAssigned_then_UPDATE_1(@Payload Assigned assigned) {
        try {
            if (assigned.isMe()) {
                // view 객체 조회
            	List<Mypage> mypageList = mypageRepository.findByOrderId(assigned.getOrderId());
                for(Mypage mypage : mypageList){
                	mypage.setShopId(assigned.getId());
                	mypage.setStatus("Assigned");
                    // view 객체에 이벤트의 eventDirectValue 를 set 함
                    // view 레파지 토리에 save
                	mypageRepository.save(mypage);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @StreamListener(KafkaProcessor.INPUT)
    public void whenOrderCancelled_then_UPDATE_2(@Payload OrderCancelled orderCancelled) {
        try {
            if (orderCancelled.isMe()) {
                // view 객체 조회
            	List<Mypage> mypageList = mypageRepository.findByOrderId(orderCancelled.getId());
            	for(Mypage mypage : mypageList){
                	mypage.setShopId(orderCancelled.getId());
                	mypage.setStatus("Refunded");
                    // view 객체에 이벤트의 eventDirectValue 를 set 함
                    // view 레파지 토리에 save
                	mypageRepository.save(mypage);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @StreamListener(KafkaProcessor.INPUT)
    public void whenRefunded_then_DELETE_1(@Payload Refunded refunded) {
        try {
            if (refunded.isMe()) {
            	List<Mypage> mypageList = mypageRepository.findByOrderId(refunded.getOrderId());
                for(Mypage mypage : mypageList){
                	mypage.setShopId(refunded.getId());
                	mypage.setStatus("Refunded");
                    // view 객체에 이벤트의 eventDirectValue 를 set 함
                    // view 레파지 토리에 save
                	mypageRepository.save(mypage);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}