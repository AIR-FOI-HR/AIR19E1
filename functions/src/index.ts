import * as functions from 'firebase-functions'
import * as admin  from 'firebase-admin'
import * as algoliasearch from 'algoliasearch'

admin.initializeApp();
const env = functions.config();

//Initialize Algolia Client
const client = algoliasearch(env.algolia.appid, env.algolia.apikey);
const index = client.initIndex('dev_NAME');

exports.indexVenue = functions.firestore
	.document('venues/{venueId}')
	.onCreate((snap, context) => {
		const data = snap.data();
		const FirebaseObjectId = snap.id;

		// Add data to Algolia index
		return index.addObject({
			FirebaseObjectId,
			...data
		});
	});

async function updateDocumentInAlgolia(change: functions.Change<FirebaseFirestore.DocumentSnapshot>) {
    const docBeforeChange = change.before.data()
    const docAfterChange = change.after.data()
    if (docBeforeChange && docAfterChange) {
        if (docAfterChange.isIncomplete && !docBeforeChange.isIncomplete) {
            // If the doc was COMPLETE and is now INCOMPLETE, it was 
            // previously indexed in algolia and must now be removed.
            await deleteDocumentFromAlgolia(change.after);
        } else if (docAfterChange.isIncomplete === false) {
            await saveDocumentInAlgolia(change.after);
        }
    }
}

async function saveDocumentInAlgolia(snapshot: any) {
    if (snapshot.exists) {
        const record = snapshot.data();
        if (record) { // Removes the possibility of snapshot.data() being undefined.
            if (record.isIncomplete === false) { // We only index products that are complete.
                record.objectID = snapshot.id;
                
                // In this example, we are including all properties of the Firestore document 
                // in the Algolia record, but do remember to evaluate if they are all necessary.
                // More on that in Part 2, Step 2 above.
                
                await index.saveObject(record); // Adds or replaces a specific object.
            }
        }
    }
}

async function deleteDocumentFromAlgolia(snapshot: FirebaseFirestore.DocumentSnapshot) {
    if (snapshot.exists) {
        const objectID = snapshot.id;
        await index.deleteObject(objectID);
    }
	}

export const collectionOnUpdate = functions.firestore.document('COLLECTION/{uid}').onUpdate(async (change, context) => {
    await updateDocumentInAlgolia(change);
});

export const collectionOnDelete = functions.firestore.document('COLLECTION/{uid}').onDelete(async (snapshot, context) => {
    await deleteDocumentFromAlgolia(snapshot);
});

