package com.graphaware.fulltext.analyzers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.util.Lists.newArrayList;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.Transaction;
import org.neo4j.harness.junit.rule.Neo4jRule;

public class PhoneticAnalyzerTest {

	@ClassRule
	public static Neo4jRule neo4j = new Neo4jRule();

	private static GraphDatabaseService graphDatabaseService;

	@BeforeClass
	public static void setUpClass() {
		graphDatabaseService = neo4j.defaultDatabaseService();

		graphDatabaseService.executeTransactionally("CALL db.index.fulltext.createNodeIndex('jobs', ['Job'], ['name'], {analyzer:'phonetic'})");
	}

	@Before
	public void setUp() {
		graphDatabaseService.executeTransactionally("MATCH (n) DETACH DELETE n");
	}

	@Test
	public void test() {
		graphDatabaseService.executeTransactionally(
				"CREATE (:Job {name: 'Secretary'}) " +
						"CREATE (:Job {name:'Financial Administrator'})"
		);
		Transaction tx = graphDatabaseService.beginTx();
		Result result = tx.execute("CALL db.index.fulltext.queryNodes('jobs', 'financial')");
		assertThat(newArrayList(result)).hasSize(1);
		result.close();

		tx = graphDatabaseService.beginTx();
		result = tx.execute("CALL db.index.fulltext.queryNodes('jobs', 'fynancial')");
		assertThat(newArrayList(result)).hasSize(1);
		result.close();

		tx = graphDatabaseService.beginTx();
		result = tx.execute("CALL db.index.fulltext.queryNodes('jobs', 'sicritary')");
		assertThat(newArrayList(result)).hasSize(1);
		result.close();
		tx.close();
	}
}
