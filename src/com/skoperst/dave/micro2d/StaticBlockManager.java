package com.skoperst.dave.micro2d;

public class StaticBlockManager{


	public StaticBlockManager() {
		// TODO Auto-generated constructor stub
	}


	final static int STATIC_BLOCK_MANAGER_UPDATEBLOCKS = 1;
	
	public void processRequests(BlockItem block) {
				processBlockUpdate(block);
		
	}
	
	
	private void processBlockUpdate(BlockItem block){
		block.mCurrentFrameIndex++;
		if ( (block.mCurrentFrameIndex % block.mFPSReduceBy) == 0){
			block.mCurrentFrameIndexReduced = (block.mCurrentFrameIndex/block.mFPSReduceBy);
			if ( block.mCurrentFrameIndexReduced >= block.mBitmapKeys.size()){
				block.mCurrentFrameIndex = 0;
				block.mCurrentFrameIndexReduced = 0;
			}
			block.mBitmapKey = block.mBitmapKeys.get(block.mCurrentFrameIndexReduced);
		}

	}


	public void processResponses() {
		// TODO Auto-generated method stub
		
	}

}
