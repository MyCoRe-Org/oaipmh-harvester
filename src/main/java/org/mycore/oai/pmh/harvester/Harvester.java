package org.mycore.oai.pmh.harvester;

import java.util.List;

import javax.xml.ws.http.HTTPException;

import org.mycore.oai.pmh.BadArgumentException;
import org.mycore.oai.pmh.BadResumptionTokenException;
import org.mycore.oai.pmh.CannotDisseminateFormatException;
import org.mycore.oai.pmh.Header;
import org.mycore.oai.pmh.IdDoesNotExistException;
import org.mycore.oai.pmh.Identify;
import org.mycore.oai.pmh.MetadataFormat;
import org.mycore.oai.pmh.NoMetadataFormatsException;
import org.mycore.oai.pmh.NoRecordsMatchException;
import org.mycore.oai.pmh.NoSetHierarchyException;
import org.mycore.oai.pmh.OAIDataList;
import org.mycore.oai.pmh.Record;
import org.mycore.oai.pmh.Set;

/**
 * <p>
 * A harvester is a client application that issues OAI-PMH requests. A harvester is operated by a service provider as a means of collecting metadata from <a
 * href="http://www.openarchives.org/OAI/openarchivesprotocol.html#Repository">repositories</a>.
 * </p>
 * You should call {@link HarvesterBuilder#createNewInstance(baseURL)} to create a construct a new instance. With this instance you can call any valid OAI-PMH
 * requests like {@link #identify()} or {@link #listMetadataFormats()}.
 * 
 * @author Matthias Eichner
 */
public interface Harvester {

    /**
     * Retrieve information about the repository.
     * 
     * @return Identify of the repository
     * @throws HTTPException
     *             The request leads to an HTTP error.
     */
    public Identify identify() throws HTTPException;

    /**
     * Retrieve the set structure of the repository.
     * 
     * @return List of sets. If a resumption token is set, the list is incomplete.
     * @throws NoSetHierarchyException
     *             The repository does not support sets.
     * @throws HTTPException
     *             The request leads to an HTTP error.
     */
    public OAIDataList<Set> listSets() throws NoSetHierarchyException, HTTPException;

    /**
     * Retrieve the set structure of the repository.
     * 
     * @param resumptionToken
     *            Flow control token returned by a previous ListSets request that issued an incomplete list
     * @return List of sets. If a resumption token is set, the list is incomplete.
     * @throws NoSetHierarchyException
     *             The repository does not support sets.
     * @throws BadResumptionTokenException
     *             The value of the resumptionToken argument is invalid or expired.
     * @throws HTTPException
     *             The request leads to an HTTP error.
     */
    public OAIDataList<Set> listSets(String resumptionToken) throws NoSetHierarchyException, BadResumptionTokenException, HTTPException;

    /**
     * Retrieve the metadata formats available from the repository.
     * 
     * @return list of metadata formats
     * @throws HTTPException
     *             The request leads to an HTTP error.
     */
    public List<MetadataFormat> listMetadataFormats() throws HTTPException;

    /**
     * Retrieve the metadata formats available from the repository for a specific item.
     * 
     * @param identifier
     *            Specifies the unique identifier of the item for which available metadata formats are being requested. The returning list includes all metadata
     *            formats supported by the repository.
     * @return list of metadata formats
     * @throws IdDoesNotExistException
     *             The value of the identifier argument is unknown or illegal in the repository.
     * @throws NoMetadataFormatsException
     *             There are no metadata formats available for the specified item.
     * @throws HTTPException
     *             The request leads to an HTTP error.
     */
    public List<MetadataFormat> listMetadataFormats(String identifier) throws IdDoesNotExistException, NoMetadataFormatsException,
            HTTPException;

    /**
     * Retrieve a bunch of headers. In difference to listRecords no metadata is contained.
     * 
     * @param metadataPrefix
     *            (required) Specifies that headers should be returned only if the metadata format matching the supplied metadataPrefix is available or,
     *            depending on the repository's support for deletions, has been deleted. The metadata formats supported by a repository and for a particular
     *            item can be retrieved using the {@link #listMetadataFormats} method. Optional parameters should be set to <code>null</code> if you don't want
     *            to use them.
     * @param from
     *            (optional) A UTCdatetime value, which specifies a lower bound for datestamp-based selective harvesting.
     * @param until
     *            (optional) A UTCdatetime value, which specifies a upper bound for datestamp-based selective harvesting.
     * @param setSpec
     *            (optional) Specifies set criteria for selective harvesting.
     * @return List of headers. If a resumption token is set, the list is incomplete.
     * @throws BadArgumentException
     *             The request includes illegal arguments or is missing required arguments.
     * @throws CannotDisseminateFormatException
     *             The value of the metadataPrefix argument is not supported by the repository.
     * @throws NoRecordsMatchException
     *             The combination of the values of the from, until, and set arguments results in an empty list.
     * @throws NoSetHierarchyException
     *             The repository does not support sets.
     * @throws HTTPException
     *             The request leads to an HTTP error.
     */
    public OAIDataList<Header> listIdentifiers(String metadataPrefix, String from, String until, String setSpec)
            throws BadArgumentException, CannotDisseminateFormatException, NoRecordsMatchException, NoSetHierarchyException, HTTPException;

    /**
     * Retrieve a bunch of headers. In difference to listRecords no metadata is contained.
     * 
     * @param resumptionToken
     *            Flow control token returned by a previous ListIdentifiers request that issued an incomplete list
     * @return List of headers. If a resumption token is set, the list is incomplete.
     * @throws BadResumptionTokenException
     *             The value of the resumptionToken argument is invalid or expired.
     * @throws HTTPException
     *             The request leads to an HTTP error.
     */
    public OAIDataList<Header> listIdentifiers(String resumptionToken) throws BadResumptionTokenException,
            HTTPException;

    /**
     * Retrieve a bunch of records.
     * 
     * @param metadataPrefix
     *            (required) Specifies that records should be returned only if the metadata format matching the supplied metadataPrefix is available or,
     *            depending on the repository's support for deletions, has been deleted. The metadata formats supported by a repository and for a particular
     *            item can be retrieved using the {@link #listMetadataFormats} method. Optional parameters should be set to <code>null</code> if you don't want
     *            to use them.
     * @param from
     *            (optional) A UTCdatetime value, which specifies a lower bound for datestamp-based selective harvesting.
     * @param until
     *            (optional) A UTCdatetime value, which specifies a upper bound for datestamp-based selective harvesting.
     * @param setSpec
     *            (optional) Specifies set criteria for selective harvesting.
     * @return List of records. If a resumption token is set, the list is incomplete.
     * @throws BadArgumentException
     *             The request includes illegal arguments or is missing required arguments.
     * @throws CannotDisseminateFormatException
     *             The value of the metadataPrefix argument is not supported by the repository.
     * @throws NoRecordsMatchException
     *             The combination of the values of the from, until, and set arguments results in an empty list.
     * @throws NoSetHierarchyException
     *             The repository does not support sets.
     * @throws HTTPException
     *             The request leads to an HTTP error.
     */
    public OAIDataList<Record> listRecords(String metadataPrefix, String from, String until, String setSpec) throws BadArgumentException,
            CannotDisseminateFormatException, NoRecordsMatchException, NoSetHierarchyException, HTTPException;

    /**
     * Retrieve a bunch of records.
     * 
     * @param resumptionToken
     *            Flow control token returned by a previous ListRecords request that issued an incomplete list
     * @return List of records. If a resumption token is set, the list is incomplete.
     * @throws BadResumptionTokenException
     *             The value of the resumptionToken argument is invalid or expired.
     * @throws HTTPException
     *             The request leads to an HTTP error.
     */
    public OAIDataList<Record> listRecords(String resumptionToken) throws BadResumptionTokenException, HTTPException;

    /**
     * Retrieve an individual metadata record from the repository.
     * 
     * @param identifier
     *            Specifies the unique identifier of the item in the repository from which the record must be disseminated.
     * @param metadataPrefix
     *            Specifies the metadataPrefix of the format that should be included in the metadata part of the returned record. A record should only be
     *            returned if the format specified by the metadataPrefix can be disseminated from the item identified by the value of the identifier argument.
     *            The metadata formats supported by a repository and for a particular record can be retrieved using the {@link #listMetadataFormats} method.
     * @return a record
     * @throws CannotDisseminateFormatException
     *             The value of the metadataPrefix argument is not supported by the item identified by the value of the identifier argument.
     * @throws IdDoesNotExistException
     *             The value of the identifier argument is unknown or illegal in this repository.
     * @throws HTTPException
     *             The request leads to an HTTP error.
     */
    public Record getRecord(String identifier, String metadataPrefix) throws CannotDisseminateFormatException,
            IdDoesNotExistException, HTTPException;

}
