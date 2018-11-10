package br.edu.ulbra.election.candidate.service;

import br.edu.ulbra.election.candidate.repository.CandidateRepository;
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
import java.util.ArrayList;
import java.util.List;

@Service
public class CandidateService {

	private final CandidateRepository candidateRepository;

	private final ModelMapper modelMapper;

	private static final String MESSAGE_INVALID_ID = "Invalid id";
	private static final String MESSAGE_CANDIDATE_NOT_FOUND = "Candidate not found";

	@Autowired
	public CandidateService(CandidateRepository candidateRepository, ModelMapper modelMapper) {
		this.candidateRepository = candidateRepository;
		this.modelMapper = modelMapper;
	}

	public List<CandidateOutput> getAll() {
		Type candidateOutputListType = new TypeToken<List<CandidateOutput>>() {
		}.getType();

		Iterable<Candidate> list = candidateRepository.findAll();

		ArrayList<CandidateOutput> lista = new ArrayList<>();

		list.forEach(e -> lista.add(Candidate.ajustarCandidates(e)));

		return modelMapper.map(lista, candidateOutputListType);
	}

	public CandidateOutput create(CandidateInput candidateInput) {
		validateInput(candidateInput);

		Candidate candidate = modelMapper.map(candidateInput, Candidate.class);
		candidate = candidateRepository.save(candidate);

		ElectionOutput election = new ElectionOutput();
		election.setId(candidate.getElectionId());

		PartyOutput party = new PartyOutput();
		party.setId(candidate.getPartyId());

		CandidateOutput candidateOutput = modelMapper.map(candidate, CandidateOutput.class);

		candidateOutput.setElectionOutput(election);
		candidateOutput.setPartyOutput(party);

		return candidateOutput;
	}

	public CandidateOutput getById(Long candidateId) {
		if (candidateId == null) {
			throw new GenericOutputException(MESSAGE_INVALID_ID);
		}

		Candidate candidate = candidateRepository.findById(candidateId).orElse(null);
		if (candidate == null) {
			throw new GenericOutputException(MESSAGE_CANDIDATE_NOT_FOUND);
		}

		return Candidate.ajustarCandidates(candidate);
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

		return Candidate.ajustarCandidates(candidate);
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
		if (StringUtils.isBlank(candidateInput.getName()) || candidateInput.getName().trim().replace(" ", "").length() < 5) {
			throw new GenericOutputException("Invalid name");
		} else {
			String name[] = candidateInput.getName().split(" ");
			if (name.length < 2) {
				throw new GenericOutputException("Invalid name");
			}
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
