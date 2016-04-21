package io.pivio.server.document;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.pivio.server.changeset.ChangesetService;
import org.elasticsearch.action.ListenableActionFuture;
import org.elasticsearch.action.delete.DeleteRequestBuilder;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.deletebyquery.DeleteByQueryRequestBuilder;
import org.elasticsearch.action.deletebyquery.DeleteByQueryResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilders;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DocumentControllerTest {

    private Client client = null;
    DocumentController documentController;
    ObjectMapper objectMapper;

    @Before
    public void setUp() {
        client = mock(Client.class);
        objectMapper = new ObjectMapper();
        documentController = new DocumentController(client, new ChangesetService(client, objectMapper), objectMapper);
    }

    @Test
    public void testDeleteNotFound() throws Exception {
        String id = "1";
        DeleteRequestBuilder mockDeleteRequestBuilder = mock(DeleteRequestBuilder.class);
        ListenableActionFuture mockListenableActionFuture = mock(ListenableActionFuture.class);
        DeleteResponse mockDeleteResponse = mock(DeleteResponse.class);

        when(client.prepareDelete("steckbrief", "steckbrief", id)).thenReturn(mockDeleteRequestBuilder);
        when(mockDeleteRequestBuilder.execute()).thenReturn(mockListenableActionFuture);
        when(mockListenableActionFuture.actionGet()).thenReturn(mockDeleteResponse);
        when(mockDeleteResponse.isFound()).thenReturn(false);

        ResponseEntity response = documentController.delete(id);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

}