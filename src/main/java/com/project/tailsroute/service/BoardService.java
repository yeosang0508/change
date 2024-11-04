package com.project.tailsroute.service;

import com.project.tailsroute.repository.BoardRepository;
import com.project.tailsroute.vo.Board;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class BoardService {

    @Autowired
    private BoardRepository boardRepository;

    public BoardService(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    public Board getBoardById(int boardId) {
        return boardRepository.getBoardById(boardId);
    }

}
