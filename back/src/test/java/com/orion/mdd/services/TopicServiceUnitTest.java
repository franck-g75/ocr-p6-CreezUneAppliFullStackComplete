package com.orion.mdd.services;

import static org.mockito.Mockito.times;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.orion.mdd.Constantes;
import com.orion.mdd.dto.TopicDto;
import com.orion.mdd.models.Post;
import com.orion.mdd.models.Topic;
import com.orion.mdd.models.UserInfo;
import com.orion.mdd.repository.TopicRepository;

@SpringBootTest
public class TopicServiceUnitTest {
@Autowired

    private TopicService topicService; //bean réel lié

    @MockitoBean
    private TopicRepository topicRepository; //bean mocké

	@Test
	public void shouldReturnAllTopics() {

        //ARRANGE    GIVEN
        LocalDateTime localDateTimeNow = LocalDateTime.now();
        Topic first = new Topic();
        first.setId(1L);
        first.setContent(Constantes.STRING_CONTENT_1);
        first.setTitle(Constantes.STRING_TITLE_1);
        UserInfo user = new UserInfo();
        user.setUsername(Constantes.USERNAME);
        user.setEmail(Constantes.EMAIL);
        HashSet<UserInfo> users = new HashSet<UserInfo>();
        users.add(user); 
        Topic second = new Topic(2L,Constantes.STRING_TITLE_2,Constantes.STRING_CONTENT_2,users,null);
        List<Topic> mockTeachers = List.of(
            first,second
        );

        Mockito.when(topicRepository.findAll()).thenReturn(mockTeachers);

        //ACT WHEN
        List<TopicDto> result = topicService.findAll();

        //ASSERT THEN
        Assertions.assertThat(2).isEqualTo(result.size());
        Assertions.assertThat(Constantes.STRING_CONTENT_1).isEqualTo(result.get(0).getContent());
        Assertions.assertThat(Constantes.STRING_CONTENT_2).isEqualTo(result.get(1).getContent());
        
        //s'asurer que la méthode findAll a bien été appelée une fois
        Mockito.verify(topicRepository, times(1)).findAll();

	}

}
