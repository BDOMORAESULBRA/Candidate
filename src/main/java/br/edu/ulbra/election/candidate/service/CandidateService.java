package br.edu.ulbra.election.candidate.service;

import br.edu.ulbra.election.candidate.repository.CandidateRepository;
import br.edu.ulbra.election.election.repository.ElectionRepository;
import br.edu.ulbra.election.party.repository.PartyRepository;
import br.edu.ulbra.election.candidate.exception.GenericOutputException;
import br.edu.ulbra.election.candidate.input.v1.CandidateInput;
import br.edu.ulbra.election.candidate.model.Candidate;
import br.edu.ulbra.election.candidate.output.v1.CandidateOutput;
import br.edu.ulbra.election.candidate.output.v1.GenericOutput;
import br.edu.ulbra.election.candidate.output.v1.ElectionOutput;
import br.edu.ulbra.election.candidate.output.v1.PartyOutput;
import org.apache.commons.lang.StringUtils;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.List;

@Service
public class CandidateService {

	private final CandidateRepository candidateRepository;

	private final ModelMapper modelMapper;

	//private final PartyRepository partyRepository;

	//private final ElectionRepository electionRepository;

	private static final String MESSAGE_INVALID_ID = "Invalid id";
	private static final String MESSAGE_CANDIDATE_NOT_FOUND = "Candidate not found";

	@Autowired
	public CandidateService(CandidateRepository candidateRepository, ModelMapper modelMapper/*,
			PartyRepository partyRepository, ElectionRepository electionRepository*/) {
		this.candidateRepository = candidateRepository;
		this.modelMapper = modelMapper;
		//this.partyRepository = partyRepository;
		//this.electionRepository = electionRepository;
	}

	public List<CandidateOutput> getAll() {
		Type candidateOutputListType = new TypeToken<List<CandidateOutput>>() {
		}.getType();
		return modelMapper.map(candidateRepository.findAll(), candidateOutputListType);
	}

	public CandidateOutput create(CandidateInput candidateInput) {
		validateInput(candidateInput);
		Candidate candidate = modelMapper.map(candidateInput, Candidate.class);
		candidate = candidateRepository.save(candidate);
		return modelMapper.map(candidate, CandidateOutput.class);
	}

	public CandidateOutput getById(Long candidateId) {
		if (candidateId == null) {
			throw new GenericOutputException(MESSAGE_INVALID_ID);
		}

		Candidate candidate = candidateRepository.findById(candidateId).orElse(null);
		if (candidate == null) {
			throw new GenericOutputException(MESSAGE_CANDIDATE_NOT_FOUND);
		}

		return modelMapper.map(candidate, CandidateOutput.class);
	}

	public CandidateOutput update(Long candidateId, CandidateInput candidateInput) {
		if (candidateId == null) {
			throw new GenericOutputException(MESSAGE_INVALID_ID);
		}
		validateInput(candidateInput);

		Candidate candidate = candidateRepository.findById(candidateId).orElse(null);
		if (candidate == null) {
			throw new GenericOutputException(MESSAGE_CANDIDATE_NOT_FOUND);
		}

		candidate.setName(candidateInput.getName());
		candidate.setNumberElection(candidateInput.getNumberElection());
		candidate.setPartyId(candidateInput.getPartyId());
		candidate.setElectionId(candidateInput.getElectionId());
		candidate = candidateRepository.save(candidate);
		return modelMapper.map(candidate, CandidateOutput.class);
	}

	public GenericOutput delete(Long canidateId) {
		if (canidateId == null) {
			throw new GenericOutputException(MESSAGE_INVALID_ID);
		}

		Candidate candidate = candidateRepository.findById(canidateId).orElse(null);
		if (candidate == null) {
			throw new GenericOutputException(MESSAGE_CANDIDATE_NOT_FOUND);
		}

		candidateRepository.delete(candidate);

		return new GenericOutput("Candidate deleted");
	}

	private void validateInput(CandidateInput candidateInput) {
		if (StringUtils.isBlank(candidateInput.getName())) {
			throw new GenericOutputException("Invalid name");
		}
		if (candidateInput.getNumberElection() == null) {
			throw new GenericOutputException("Invalid number");
		}
		if (candidateInput.getPartyId() == null) {
			throw new GenericOutputException("Invalid party");
		}
		if (candidateInput.getElectionId() == null) {
			throw new GenericOutputException("Invalid election");
		}
	}

}
