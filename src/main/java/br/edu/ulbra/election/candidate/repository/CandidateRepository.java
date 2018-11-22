package br.edu.ulbra.election.candidate.repository;

import br.edu.ulbra.election.candidate.model.Candidate;
import org.springframework.data.repository.CrudRepository;

public interface CandidateRepository extends CrudRepository<Candidate, Long> {

	Candidate findFirstByNumberElectionAndElectionId(Long numberElection, Long electionId);

	Candidate findFirstByElectionId(Long electionId);

	Candidate findFirstByPartyId(Long partyId);

}
