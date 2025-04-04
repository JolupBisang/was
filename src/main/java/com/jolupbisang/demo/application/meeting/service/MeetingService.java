package com.jolupbisang.demo.application.meeting.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jolupbisang.demo.application.meeting.dto.AudioMeta;
import com.jolupbisang.demo.application.meeting.dto.MeetingReq;
import com.jolupbisang.demo.application.meeting.exception.MeetingErrorCode;
import com.jolupbisang.demo.domain.agenda.Agenda;
import com.jolupbisang.demo.domain.meeting.Meeting;
import com.jolupbisang.demo.domain.meetingUser.MeetingUser;
import com.jolupbisang.demo.domain.meetingUser.MeetingUserStatus;
import com.jolupbisang.demo.domain.user.User;
import com.jolupbisang.demo.global.exception.CustomException;
import com.jolupbisang.demo.global.properties.MeetingProperties;
import com.jolupbisang.demo.infrastructure.agenda.AgendaRepository;
import com.jolupbisang.demo.infrastructure.meeting.MeetingRepository;
import com.jolupbisang.demo.infrastructure.meetingUser.MeetingUserRepository;
import com.jolupbisang.demo.infrastructure.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MeetingService {

    private final UserRepository userRepository;
    private final MeetingRepository meetingRepository;
    private final MeetingUserRepository meetingUserRepository;
    private final AgendaRepository agendaRepository;

    private final MeetingProperties meetingProperties;
    private final ObjectMapper objectMapper;

    @Transactional
    public void createMeeting(MeetingReq meetingReq, Long userId) {
        User leader = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(MeetingErrorCode.NOT_FOUND));

        Meeting meeting = meetingReq.toEntity();
        meetingRepository.save(meeting);

        List<User> participants = userRepository.findByEmailIn(meetingReq.participants());
        meetingUserRepository.save(new MeetingUser(meeting, leader, true, MeetingUserStatus.ACCEPTED));
        meetingUserRepository.saveAll(participants.stream().map(p ->
                new MeetingUser(meeting, p, false, MeetingUserStatus.WAITING)).toList());

        agendaRepository.saveAll(meetingReq.agendas().stream().map(agenda ->
                new Agenda(meeting, agenda)).toList());
    }

    public void processAndSendAudioData(WebSocketSession session, BinaryMessage message) throws IOException {
        ByteBuffer buffer = message.getPayload();
        AudioMeta audioMeta = extractAudioMeta(buffer);
        log.info("{}", audioMeta);
        byte[] audioData = extractAudioData(buffer);
        log.info("{}", audioData);
        saveAudio(audioMeta, audioData);
    }

    private void saveAudio(AudioMeta audioMeta, byte[] audioData) throws IOException {
        Path dirPath = Path.of(meetingProperties.getBaseDir(),
                Long.toString(audioMeta.meetingId()),
                Long.toString(audioMeta.userId()));
        String filename = Integer.toString(audioMeta.chunkId());

        if (!Files.exists(dirPath)) {
            Files.createDirectories(dirPath);
        }

        File chunkFile = dirPath.resolve(filename).toFile();

        try (FileOutputStream fos = new FileOutputStream(chunkFile)) {
            fos.write(audioData);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private AudioMeta extractAudioMeta(ByteBuffer byteBuffer) {
        int metaLength = byteBuffer.getInt();
        byte[] metaData = new byte[metaLength];
        byteBuffer.get(metaData);
        String metaString = new String(metaData, StandardCharsets.UTF_8);

        try {
            return objectMapper.readValue(metaString, AudioMeta.class);
        } catch (JsonProcessingException ex) {
            log.error("Error parsing JSON: {}", ex.getMessage());
            return null;
        }
    }

    private byte[] extractAudioData(ByteBuffer byteBuffer) {
        byte[] audioBytes = new byte[byteBuffer.remaining()];
        byteBuffer.get(audioBytes);

        return audioBytes;
    }
}
