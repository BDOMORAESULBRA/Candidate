package br.edu.ulbra.election.candidate.model;

import javax.persistence.*;

import br.edu.ulbra.election.candidate.output.v1.CandidateOutput;
import br.edu.ulbra.election.candidate.output.v1.ElectionOutput;
import br.edu.ulbra.election.candidate.output.v1.PartyOutput;

@Entity
public class Candidate {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	private Long party_id;

	@Column(nullable = false)
	private Long number;

	@Column(nullable = false)
	private Long election_id;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getNumberElection() {
		return number;
	}

	public void setNumberElection(Long numberElection) {
		this.number = numberElection;
	}

	public Long getElectionId() {
		return election_id;
	}

	public void setElectionId(Long electionid) {
		this.election_id = electionid;
	}

	public Long getPartyId() {
		return party_id;
	}

	public void setPartyId(Long partyid) {
		this.party_id = partyid;
	}

	public static CandidateOutput ajustarCandidates(Candidate candidate) {

		ElectionOutput election = new ElectionOutput();
		election.setId(candidate.getElectionId());

		PartyOutput party = new PartyOutput();
		party.setId(candidate.getPartyId());

		CandidateOutput candidateOutput = new CandidateOutput();

		candidateOutput.setId(candidate.getId());
		candidateOutput.setName(candidate.getName());
		candidateOutput.setNumberElection(candidate.getNumberElection());
		candidateOutput.setElectionOutput(election);
		candidateOutput.setPartyOutput(party);

		return candidateOutput;

	}

}
